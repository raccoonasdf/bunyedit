0.7.1
-----
- Updated for BTA 7.1. No meaningful changes from 0.7.0
- "7.1" in our version string for the BTA 7.1 update, isn't that fun :)

0.7.0
-----
- Updated dependencies. Now building against BTA 7.1-pre2a (PR by UselessBullets)
- Added selection limits. If your selection exceeds a certain volume
  (131072 by default), commands involving it will fail. You can use
  `//limit [int|"no"]` to change the limit
- Fixed `//flip ^`
- Fixed cursor selection when `doDaylightCycle` is false

0.6.0
-----
- Fixed `//selection` output
- A selection that exists in a different world from the player's current world is now considered invalid
- Players now have a seperate undo buffer for each world they edit in
- Adjusted max cursor range to something more reasonable (50 -> 256)
- Fixed `//flip` with axis argument
- Fixed `//move` where source and destination are overlapping regions

0.5.1
-----
- Fixed crash when trying to use the cursor to select a block that's out
  of range

0.5.0
-----
- Added disambiguation for block patterns (if you type `water`, you will
  be warned in chat but the command will continue as if you typed
  `fluid.water.still`; if you type `sponge`, the command will error out
  and you will receive the suggestion to append either `.dry` or `.wet`;
  etc.)
- `bound` argument (as for `//growsel`) given new semantics: default
  magnitude is 0 instead of 1 (`*` does nothing, `*1` grows by 1 in all
  directions), and specifying the same direction more than once
  overwrites rather than adds
- Added `//fill <filter> <pattern> [bound=*16]`

0.4.1
-----
- Fixed `//growsel` local directions (`F|B|L|R`) (B and R had the wrong
  orientation and were the same as F and L)

0.4.0
-----
- Blocks are no longer notified of newly placed neighbor blocks until
  the entire buffer is done being placed
- `onBlockRemoved` and `onBlockAdded` events are no longer triggered
  by editor actions
- `//flip` now reorients blocks that can be placed directionally
- Masks can now take arguments. No additional functionality for now, but
  hollow variants are now specified as e.g. `//mask cube h` instead of
  `//mask hcube`
- Added `//growsel`
```
//growsel [grow]

grow: <growDir>[,<growDir>...]

growDir: (S|N|E|W|U|D|F|B|L|R|*)[<int>]
```

0.3.0
-----
- Added support for specifying blocks by ID in commands
- Added expanded filter syntax:
```
<filter>[/<filter>[...]]

filter: [!](<block>|#<material>)

block: <id>[:<meta>]

id: (<int>|<range>|<key>|*)
meta: (<int>|<range>|*)

range: (<int>..<int>|<int>..|..<int>)
key: <str>[.<str>[...]][;]
```
- Added config file
- Added rudimentary permissions system: can be configured to let players
  in specific gamemodes access bunyedit
- Added weighted random pattern type:
```
<weightedBlock>[/<weightedBlock>[...]]

weightedBlock: [<int>*]<block>
```

0.2.0
-----
- Restructured command code to make player and selection validity checks
  universal
- Changed coordinate format to `<x>,<y>,<z>` (comma-seperated) for easier
  parsing as a single argument
- Added `^<sway>,^<heave>,^<surge>` look-direction relative coordinates
- Added `//move` and `//movesel`
- Added `//stack`
- Added `//flip`

0.1.0
-----
- Initial release
- Added selections (`//cursor`, `//1`, `//2`, `//selection`)
- Added masks (`//mask`)
- Added set command (`//set`)
- Added undo history (`//undo`, `//redo`)
- Added copy buffer (`//copy`, `//paste`)
