//
//  ApplePencilDetector.h
//  iosApp
//
//  Created by Ferdinand Bada on 18/10/2024.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@protocol ApplePencilDetectorDelegate <NSObject>
- (void)pencilConnectionDidChange:(BOOL)isConnected;
- (void)pencilDrawingStateDidChange:(BOOL)isDrawing;
@end

@interface ApplePencilDetector : NSObject

@property (nonatomic, weak) id<ApplePencilDetectorDelegate> delegate;
@property (nonatomic, readonly) BOOL isPencilConnected;
@property (nonatomic, readonly) BOOL isDrawing;

+ (instancetype)shared;
- (void)startMonitoring;
- (void)stopMonitoring;

@end

NS_ASSUME_NONNULL_END
