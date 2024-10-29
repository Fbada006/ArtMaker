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

import android.content.Context

/**
 * We know what we are doing holding this context here (Hopefully)
 */
object ContextProvider {

    private var applicationContext: Context? = null

    fun setContext(context: Context) {
        applicationContext = context
    }

    fun getContext(): Context = checkNotNull(applicationContext) {
        "The application context should not be null. Please call fun setApplicationContext(context: Context) first"
    }
}
