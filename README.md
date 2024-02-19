# bunyedit

hi! this is a minecraft mod for better than adventure. it's my own take on a
worldedit-like :)

here's a video showing some features:

https://github.com/raccoonasdf/bunyedit/assets/16406874/f89a8df0-1b98-48fb-95a9-5203d2025e68

someone is already doing this (it's called
[bta-worldedit](https://github.com/FatherCheese/bta-worldedit)) but i wanted to try it
myself. thank you to the author of that for inspiration!

this is not a fork of either worldedit or bta-worldedit, but i have read bits of their
code.

## building and installing

please read [BTA Wiki: Modding](https://bta.miraheze.org/wiki/Modding) if you don't know
how to set up your editor for this. building should be simple after that. i'll probably
have prebuilt jars once this is a little less experimental.

you have to install the mod on the client to use it in singleplayer, but only the server
needs it in multiplayer.

## differences from bta-worldedit

1. the cursor in bunyedit uses left/right click and works from a distance like original
   worldedit. the wand in bta-worldedit uses right/shift-right click and requires that
   the block is in reach. the former feels more familiar to me, but forces bunyedit to
   mixin a little more invasively.
2. bunyedit is heavily inspired by worldedit but i won't shy away from doing my own take
   on things. bta-worldedit seems to be very much going for replicating the same
   interface as worldedit.
3. bunyedit tries its best to play nice with tile entities.

## documentation

i'll get around to doing real docs when this is 1.0 quality.

here's the gist for now:
1. use `//cursor` to get the cursor item.
2. with the cursor in your paw, left click and right click to select the corners of the
   cuboid you want to operate on.
3. use `//set`, `//copy`, and `//paste` to do something with that selection.
4. use `//undo` and `//redo` if you mess up.

note that we prefer to avoid creating too many top-level commands so some features are
exposed differently from worldedit:

1. `//set` and `//replace` are really the same command: `//set [filter] <pattern>`
2. shapes are created using the 'selection masking' feature, which modifies selection
   behavior for *all commands*. the default shape is `cuboid`, but you can do
   `//mask <shape>` to get a `spheroid`, `hcuboid` (hollow cuboid), et cetera.
   try `//mask list` :)
