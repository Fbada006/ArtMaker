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
package io.fbada006.artmaker

import android.content.Context
import io.fbada006.artmaker.utils.ContextProvider

/**
 * Has to be called with a valid context from Android in order to initialise the android side
 */
object ArtMakerInitializer {

    fun initialise(context: Context) {
        ContextProvider.setContext(context)
    }
}
