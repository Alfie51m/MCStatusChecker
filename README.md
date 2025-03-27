# MCStatusChecker

**MCStatusChecker** is a simple Spigot plugin for Minecraft servers running version 1.21 or later. This plugin allows you to define custom messages sent to a player depending on the result of a placeholder when mentioning a username in Minecraft chat. 

## Example Config

```yaml
verbose: true # Set to false to disable detailed logs

afk_check:
  placeholder: "%essentials_afk%"
  expected_result: "yes"
  success_message: "&a{player} is AFK!"
  failure_message: "&c{player} is not AFK." # Set to "disabled" to not send a message on false output

placeholder2:
  placeholder: "%otherplugin_someplaceholder%"
  expected_result: "true"
  success_message: "&a{player} is Here!"
  failure_message: "disabled" # No failure message for this placeholder
```

## Installation
1. Download the latest release from the [Releases](https://github.com/Alfie51m/MCStatusChecker/releases) section.
2. Place the `.jar` file in your server's `plugins` folder.
3. Restart your server.

## Contributing
Feel free to submit issues or contribute via pull requests on the [GitHub repository](https://github.com/Alfie51m/MCStatusChecker).

## License
This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt).

--- 

### **Thanks for using MCStatusChecker**
