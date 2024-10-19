//
//  PencilGestureRecognizer.h
//  iosApp
//
//  Created by Ferdinand Bada on 18/10/2024.
//

#import <UIKit/UIKit.h>

@interface PencilGestureRecognizer : UIGestureRecognizer
@property (nonatomic, copy) void (^pencilTouchHandler)(BOOL isDrawing);
@end
