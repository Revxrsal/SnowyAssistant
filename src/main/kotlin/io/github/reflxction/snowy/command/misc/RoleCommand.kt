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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.reflxction.snowy.command.misc

import io.github.reflxction.snowy.command.core.BotCommand
import io.github.reflxction.snowy.command.core.CommandCallback
import io.github.reflxction.snowy.command.core.CommandCategory
import io.github.reflxction.snowy.command.core.CommandContext
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Role

@BotCommand(
        name = "role",
        parameters = "<role>",
        description = "Get information about a specific role",
        category = CommandCategory.DEVELOPER,
        requiredPermissions = [Permission.ADMINISTRATOR],
        minimumArguments = 1
)
object RoleCommand : CommandCallback {

    /**
     * Processes the command
     */
    override fun onProcess(context: CommandContext) {
        val role = context.resolve(context.argsString, Role::class.java)
        context.reply(EmbedBuilder()
                .setTitle(role.name)
                .setColor(role.color)
                .setDescription("**ID**: ${role.id}\n**Position**: ${role.position}\n**Permissions**: ${role.permissions.joinToString(", ")}"))
    }

}