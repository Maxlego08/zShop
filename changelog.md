# Unreleased

# 3.0.9

- Fix sellall message
- Add option for /sell <all/hand/handall>

# 3.0.8

- Update to zMenu 1.0.2.7
- Fix /sell-hand command
- Fix sell lore confirm

# 3.0.7

- Fix error with item lore

# 3.0.6

- Fix error with MenuItemStack cache on purchase and sell items
- Added ``economy`` configuration for each inventory. You can now choose the default economy by file 

# 3.0.5

- Fix error with lore

# 3.0.4

- Correction of price display during purchase and sale, taking into account events.
- Ajout de l'économie BeastToken
- Fix NPE with ``ShopManger#getItemButton`` method

# 3.0.3

- Add ``unstackable`` tag for ZSHOP_ITEM, allows you to choose the number of items to buy but not receive them stacked. Perfect for potions for example.

# 3.0.2

- Add inventory name for methods ``openBuy``, ``openSell`` and ``openConfirm``
- Add inventory name in ZSHOP_ITEM button, you can now specify the inventory for buy, sell and confirm 