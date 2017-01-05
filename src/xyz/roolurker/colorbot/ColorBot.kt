package xyz.roolurker.colorbot

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.Role
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

/**
 * Created with love by rooLurk on 1/5/17.
 */

/**
 * Copyright (c) 2017 Markus "rooLurk" Isberg
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

class ColorBot(token: String) : ListenerAdapter() {
	val bot: JDA = JDABuilder(AccountType.BOT)
			.addListener(this)
			.setToken(token)
			.buildBlocking()

	val commandChan: String = "266598610659966976"

	override fun onMessageReceived(e: MessageReceivedEvent?) {
		super.onMessageReceived(e)
		if (e == null) {
			return
		} else if (e.channel.id != commandChan || e.author.isBot) {
			return
		}
		val msg: String = e.message.rawContent
		val split: List<String> = msg.split(" ", limit = 2)
		if (split.isNotEmpty()) {
			if ("!color" == split[0] || "!colour" == split[0]) {
				val channel: MessageChannel = e.channel
				val member: Member = e.member
				val par2: String = split[1]
				if (split.size == 2) {
					if ("remove" == par2) {
						val oldRole: Role? = getColorRole(member)
						if (oldRole == null) {
							channel.sendMessage("${member.asMention} You do not have a color!").queue()
							return
						} else {
							e.guild.controller.removeRolesFromMember(member, oldRole).queue({
								channel.sendMessage("${member.asMention} Done!").queue()
							})
							return
						}
					}
					try {
						val i: Int = par2.toInt()
						val guild: Guild = e.guild
						val hex: String? = indexToHex(i)
						if (hex == null) {
							channel.sendMessage("${member.asMention} Invalid index.\nUsage: `!color <index>`. See channel topic for available colors.").queue()
						} else {
							for (role in guild.roles) {
								if (hex == role.name && role.permissionsRaw == 0x00000000L) {
									addRole(guild, hex, member, channel)
									return
								}
							}
							channel.sendMessage("${member.asMention} Role could not be found, this is an issue on the guild's end.").queue()
							return
						}
					} catch (e: NumberFormatException) {
						channel.sendMessage("${member.asMention} Invalid index.\nUsage: `!color <index>`. See channel topic for available colors.").queue()
						return
					}
				} else {
					channel.sendMessage("Usage: `!color <index>`. See channel topic for available colors.").queue()
					return
				}
			}
		}
	}

	fun addRole(guild: Guild, hex: String, member: Member, channel: MessageChannel) {
		val oldRole: Role? = getColorRole(member)

		if (oldRole != null) {
			channel.sendMessage("${member.asMention} You already have a color! Please remove it with `!color remove` first.").queue()
			return
		}
		for (i in guild.roles) {
			if (i.name == hex) {
				guild.controller.addRolesToMember(member, i).queue({
					channel.sendMessage("${member.asMention} Done! Enjoy your new color.").queue()
				})
				return
			}
		}
	}

	fun getColorRole(member: Member): Role? {
		return member.roles.firstOrNull { it.name.startsWith("#") && it.name.length == 7 && it.permissionsRaw == 0x00000000L}
	}

	fun indexToHex(index: Int): String? {
		when (index) {
			1 -> return "#FFFFFF"
			2 -> return "#010101"
			3 -> return "#C0C0C0"
			4 -> return "#808080"
			5 -> return "#87CEEB"
			6 -> return "#008FFF"
			7 -> return "#4169E1"
			8 -> return null // "#0033FF"
			9 -> return null // "#00058F"
			10 -> return null // "#000550"
			11 -> return "#90FFFF"
			12 -> return "#00FFFF"
			13 -> return "#40E0D0"
			14 -> return "#05C2C5"
			15 -> return "#008080"
			16 -> return "#00FA9A"
			17 -> return "#00FFCC"
			18 -> return "#3CB371"
			19 -> return "#1C9451"
			20 -> return "#ADFF2F"
			21 -> return "#00FF00"
			22 -> return "#3EBB2D"
			23 -> return "#008000"
			24 -> return "#A5D769"
			25 -> return "#FAD7AE"
			26 -> return "#EB9A3B"
			27 -> return "#FF9000"
			28 -> return "#CF7A27"
			29 -> return "#C96016"
			30 -> return "#A0522D"
			31 -> return "#EC8F6A"
			32 -> return "#EC7171"
			33 -> return "#FF5A3D"
			34 -> return "#FF4500"
			35 -> return "#FF0000"
			36 -> return null // "#970A04"
			37 -> return "#DB133B"
			38 -> return "#FFFF00"
			39 -> return "#E6B907"
			40 -> return "#FCAAB9"
			41 -> return "#FF69B4"
			42 -> return "#FF1493"
			43 -> return "#D3138C"
			44 -> return "#AC25B1"
			45 -> return "#FF00FF"
			46 -> return null // "#960F96"
			47 -> return "#A869FF"
			48 -> return "#8A2BE2"
			49 -> return "#BB55FF"
			50 -> return "#6654E6"
		}
		return null
	}
}