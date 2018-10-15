### Download apk
https://puu.sh/BLnFn/9533e7ce25.apk

### Overview

This project is a pretty bare bone way of finding rapidvideo links from http://kissanime.ru/M/ (mobile site)

Main purpose is to make watching anime on mobile simpler and easier


### Cloudflare
This was the main obstacle when creating this project. You need to wait ~5s before the page loads, then solve js challenge forward it to the cloudflare instance and manually handle redirects.

This is why it take so long for everything to load, both searches and links, but after they are loaded they get saved in `/sdcard/Kissanime/`, from where you can later just open then and watch.


### Features

###### Main Tab
Here in the middle you will have your last opened file ready to to and its title displayed.

###### Url Tab
In this tab you have `Search` and `Load` options.

`Search` - will allow you to query using the same search that kissanime.ru has. It will be paused till the query finishes. After its done it will show the results in TextView autocomplete list.

`Load` - you can load any direct mobile url to given anime. Clicking on autocomplete options will automatically input the url.

###### File Tab
`File` - opens the most basic file picker, you need to select your anime .json file then you will be tranported to another activity with this anime loaded.


###### Anime Tab
Here you get a simple list of all the anime episodes that were loaded form your file, click will prompt browser picker and grey out the episode, adding it to watched. Long click can remove them from already watched ones.

All the watched episodes id (few digits) will be saved in SharedPreference so you can swap between files and watched episodes will stay.

### Screenshots
![Main Tab](https://media.discordapp.net/attachments/339854401663664138/501363175766360064/unknown.png?width=169&height=300 "Main Tab") ![Url Tab]( https://media.discordapp.net/attachments/339854401663664138/501363357773725696/unknown.png?width=169&height=300 "Search Tab") ![Downloading]( https://media.discordapp.net/attachments/339854401663664138/501363464963489792/unknown.png?width=169&height=300 "Downloading") ![Finished]( https://media.discordapp.net/attachments/339854401663664138/501363735374462976/unknown.png?width=169&height=300 "Finished") ![File Tab]( https://media.discordapp.net/attachments/339854401663664138/501363848906014720/unknown.png?width=169&height=300 "File Tab") ![Anime Tab](https://media.discordapp.net/attachments/339854401663664138/501364011678564364/unknown.png?width=169&height=300 "Anime Tab") ![Json content]( https://media.discordapp.net/attachments/339854401663664138/501364324414259203/unknown.png?width=169&height=300 "Json content")

### End
I know this app looks extremaly basic from the graphical part, but the amount of work that went into the programming side took a lot of time and i dont mind simple GUI

### Future
I might or might not be updating this project, but if you email me ill try to check why it doesnt work or if i can add something

### Copyright &copy;
Feel free to use any part of this code for yourself, or further develop this app but id be happy if you would contact me first: iwaniuk.krzysztof@outlook.com

Maybe we can work on something together ^^
