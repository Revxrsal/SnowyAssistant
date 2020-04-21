/*
 * * Copyright 2019-2020 github.com/ReflxctionDev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing requiredPermissions and
 * limitations under the License.
 */
package io.github.reflxction.snowy.command.core

import net.dv8tion.jda.api.Permission

class CommandWrapper(val name: String,
                     val description: String,
                     val parameters: String,
                     val aliases: List<String>,
                     val permissions: Array<Permission>,
                     val roles: Array<String>,
                     val help: Boolean,
                     val category: CommandCategory,
                     val minimumArguments: Int,
                     val command: CommandCallback)