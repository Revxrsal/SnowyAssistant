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
package io.github.reflxction.snowy.command.admin

import io.github.reflxction.snowy.SnowyBot
import io.github.reflxction.snowy.command.core.BotCommand
import io.github.reflxction.snowy.command.core.CommandCallback
import io.github.reflxction.snowy.command.core.CommandCategory
import io.github.reflxction.snowy.command.core.CommandContext
import io.github.reflxction.snowy.listener.StandardReply

@BotCommand(
        name = "reply",
        parameters = "<text>%<reply>",
        category = CommandCategory.ADMIN,
        description = "Add a standard reply",
        help = false,
        minimumArguments = 1,
        allowedRoles = ["698533331477856256", "634664923103887362", "635451989467070465"]
)
object AddReplyCommand : CommandCallback {

    /**
     * Processes the command
     */
    override fun onProcess(context: CommandContext) {
        if (!context.argsString.contains("%"))
            throw CommandCallback.CommandException("You must split the required reply text and the reply text with a `%`.")
        val reply = context.argsString.split("%".toRegex(), 2)
        SnowyBot.getDefaultReplies().replies.add(StandardReply(reply[0], reply[1]))
        context.reply("Reply added.")
    }
}