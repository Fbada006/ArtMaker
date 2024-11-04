/*
 * Copyright 2024 ArtMaker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fbada006.artmaker.utils

/**
 * This method takes a numerical value and ensures it fits in a given numerical range. If the
 * number is smaller than the minimum required by the range, then the minimum of the range will
 * be returned. If the number is higher than the maximum allowed by the range then the maximum
 * of the range will be returned.
 *
 * @param value the value to be clamped.
 * @param min minimum resulting value.
 * @param max maximum resulting value.
 *
 * @return the clamped value.
 */
internal fun clamp(value: Float, min: Float, max: Float): Float = when {
    value < min -> min
    value > max -> max
    else -> value
}
