//
//  ApplePencilDetector.m
//  iosApp
//
//  Created by Ferdinand Bada on 18/10/2024.
//

// ApplePencilDetector.m
#import "ApplePencilDetector.h"
#import "PencilGestureRecognizer.h"

@interface ApplePencilDetector ()
@property (nonatomic) BOOL isPencilConnected;
@property (nonatomic) BOOL isDrawing;
@property (nonatomic, strong) PencilGestureRecognizer *pencilGesture;
@end

@implementation ApplePencilDetector

+ (instancetype)shared {
    static ApplePencilDetector *sharedInstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[ApplePencilDetector alloc] init];
    });
    return sharedInstance;
}

- (instancetype)init {
    if (self = [super init]) {
        [self setup];
    }
    return self;
}

- (void)setup {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(pencilConnectionChanged:)
                                               name:@"UIPencilDidConnectNotification"
                                             object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(pencilConnectionChanged:)
                                               name:@"UIPencilDidDisconnectNotification"
                                             object:nil];
}

- (UIWindow *)mainWindow {
    UIWindow *window = nil;
    
    if (@available(iOS 13.0, *)) {
        NSSet<UIScene *> *connectedScenes = UIApplication.sharedApplication.connectedScenes;
        for (UIScene *scene in connectedScenes) {
            if (scene.activationState == UISceneActivationStateForegroundActive && [scene isKindOfClass:[UIWindowScene class]]) {
                UIWindowScene *windowScene = (UIWindowScene *)scene;
                window = windowScene.windows.firstObject;
                break;
            }
        }
    } else {
        window = UIApplication.sharedApplication.keyWindow;
    }
    
    return window;
}

- (void)startMonitoring {
    if (@available(iOS 13.0, *)) {
        UIWindow *window = [self mainWindow];
        if (!window) return;
        
        // Create and store the gesture recognizer
        self.pencilGesture = [[PencilGestureRecognizer alloc] init];
        __weak typeof(self) weakSelf = self;
        self.pencilGesture.pencilTouchHandler = ^(BOOL isDrawing) {
            __strong typeof(weakSelf) strongSelf = weakSelf;
            if (strongSelf) {
                strongSelf.isDrawing = isDrawing;
                [strongSelf.delegate pencilDrawingStateDidChange:isDrawing];
            }
        };
        [window addGestureRecognizer:self.pencilGesture];
        
        // Add pencil interaction for system features
        UIPencilInteraction *pencilInteraction = [[UIPencilInteraction alloc] init];
        [window addInteraction:pencilInteraction];
    }
}

- (void)stopMonitoring {
    if (@available(iOS 13.0, *)) {
        UIWindow *window = [self mainWindow];
        if (!window) return;
        
        // Remove pencil interactions
        for (UIPencilInteraction *interaction in window.interactions) {
            if ([interaction isKindOfClass:[UIPencilInteraction class]]) {
                [window removeInteraction:interaction];
            }
        }
        
        // Remove our gesture recognizer
        if (self.pencilGesture) {
            [window removeGestureRecognizer:self.pencilGesture];
            self.pencilGesture = nil;
        }
    }
}

- (void)pencilConnectionChanged:(NSNotification *)notification {
    BOOL wasConnected = self.isPencilConnected;
    self.isPencilConnected = [notification.name isEqualToString:@"UIPencilDidConnectNotification"];
    
    if (wasConnected != self.isPencilConnected) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate pencilConnectionDidChange:self.isPencilConnected];
        });
    }
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [self stopMonitoring];
}

@end
