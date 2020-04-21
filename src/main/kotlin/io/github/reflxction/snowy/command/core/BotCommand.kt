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

/**
 * Represents a command
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class BotCommand(

        /**
         * The command name
         */
        val name: String,

        /**
         * The command description
         */
        val description: String,

        /**
         * The command parameters
         */
        val parameters: String = "",

        /**
         * The command category
         */
        val category: CommandCategory,

        /**
         * The command aliases
         */
        val aliases: Array<String> = [],

        /**
         * The minimum arguments required to run this command
         */
        val minimumArguments: Int = 0,

        /**
         * Whether should the command appear in the help menu or not
         */
        val help: Boolean = true,

        /**
         * Permissions required to run this command
         */
        val requiredPermissions: Array<Permission> = [],

        /**
         * The IDs of the roles allowed to use this command
         */
        val allowedRoles: Array<String> = []
)