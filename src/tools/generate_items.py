import json

with open(r"C:\Users\Oskar\Documents\GitHub\RogueScape\src\main\raw_data\items-complete.json", "r", encoding="utf-8") as f:
    items = json.load(f)

output = {}

for item_id, item in items.items():
    if item.get("noted"):
        continue
    if item.get("placeholder"):
        continue
    if item.get("duplicate"):
        continue
    if not item.get("name"):
        continue

    output[item_id] = {
        "id": item_id,
        "name": item["name"],
        "imageUrl": item.get("icon")
    }

with open("../main/sorted_data/items_filtered.json", "w", encoding="utf-8") as f:
    json.dump(output, f, indent=2)

print(f"Generated {len(output)} filtered items")