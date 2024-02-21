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
