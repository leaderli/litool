/*
 * Copyright (C) 2011 Google Inc.
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

package io.leaderli.litool.core.lang.lean;


import io.leaderli.litool.core.type.LiTypeToken;


/**
 * TypeAdapterFactory 接口表示一个类型适配器工厂，用于生成类型适配器。
 */
public interface TypeAdapterFactory {

    /**
     * 根据给定类型和lean对象，返回对应的类型适配器实例。
     *
     * @param <T>  参数T表示类型参数
     * @param lean lean对象
     * @param type 类型
     * @return 返回类型适配器
     */
    <T> TypeAdapter<T> create(Lean lean, LiTypeToken<T> type);
}
