//
//  PencilGestureRecognizer.m
//  iosApp
//
//  Created by Ferdinand Bada on 18/10/2024.
//

#import "PencilGestureRecognizer.h"

@implementation PencilGestureRecognizer

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [super touchesBegan:touches withEvent:event];
    
    for (UITouch *touch in touches) {
        if (touch.type == UITouchTypePencil) {
            if (self.pencilTouchHandler) {
                self.pencilTouchHandler(YES);
            }
            break;
        }
    }
}

- (void)touchesEnded:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [super touchesEnded:touches withEvent:event];
    
    for (UITouch *touch in touches) {
        if (touch.type == UITouchTypePencil) {
            if (self.pencilTouchHandler) {
                self.pencilTouchHandler(NO);
            }
            break;
        }
    }
}

- (void)touchesCancelled:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [super touchesCancelled:touches withEvent:event];
    
    if (self.pencilTouchHandler) {
        self.pencilTouchHandler(NO);
    }
}

@end
