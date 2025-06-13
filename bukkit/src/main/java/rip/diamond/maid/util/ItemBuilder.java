package rip.diamond.maid.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.persistence.PersistentDataType;
import rip.diamond.maid.Maid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemBuilder {

	public static final NamespacedKey NAMESPACED_KEY_DATA = new NamespacedKey(Maid.INSTANCE, "data");
	private final ItemStack is;

	public ItemBuilder(Material mat) {
		is = new ItemStack(mat);
	}

	public ItemBuilder(ItemStack is) {
		this.is = is.clone();
	}

	public ItemBuilder(ItemStack is, boolean clone) {
		if (!clone) {
			this.is = is;
			return;
		}
		this.is = is.clone();
	}

	public ItemBuilder amount(int amount) {
		is.setAmount(amount);
		return this;
	}

	public ItemBuilder material(Material material) {
		is.setType(material);
		return this;
	}

	public ItemBuilder name(String name) {
		ItemMeta meta = is.getItemMeta();
		meta.displayName(MiniMessage.miniMessage().deserialize("<!italic>" + name));
		is.setItemMeta(meta);
		return this;
	}

	public ItemBuilder lore(int index, String string) {
		ItemMeta meta = is.getItemMeta();
		List<Component> lore = meta.lore();

		if (lore == null) {
			lore = new ArrayList<>();
		}

		lore.add(index, MiniMessage.miniMessage().deserialize("<!italic>" + string));

		meta.lore(lore);
		is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder lore(String... strings) {
		ItemMeta meta = is.getItemMeta();
		List<Component> lore = meta.lore();

		if (lore == null) {
			lore = new ArrayList<>();
		}

		for (String str : strings) {
			if (str != null) {
				lore.add(MiniMessage.miniMessage().deserialize("<!italic>" + str));
			}
		}

		meta.lore(lore);
		is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder lore(List<String> strings) {
		if (strings == null) {
			return this;
		}
		ItemMeta meta = is.getItemMeta();
		List<Component> lore = meta.lore();

		if (lore == null) {
			lore = new ArrayList<>();
		}

		for (String str : strings) {
			if (str != null) {
				lore.add(MiniMessage.miniMessage().deserialize("<!italic>" + str));
			}
		}

		meta.lore(lore);
		is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder setLore(List<String> strings) {
		if (strings == null) {
			return this;
		}
		ItemMeta meta = is.getItemMeta();
		List<Component> lore = new ArrayList<>();

		for (String str : strings) {
			if (str != null) {
				lore.add(MiniMessage.miniMessage().deserialize("<!italic>" + str));
			}
		}

		meta.lore(lore);
		is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder damage(int durability) {
		Damageable meta = (Damageable) is.getItemMeta();
		meta.setDamage(durability);
		is.setItemMeta(meta);
		return this;
	}

	public ItemBuilder enchantments(Map<Enchantment, Integer> enchantments) {
		this.is.addEnchantments(enchantments);
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment, int level) {
		is.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment) {
		is.addUnsafeEnchantment(enchantment, 1);
		return this;
	}

	public ItemBuilder safeEnchantment(Enchantment enchantment, int level) {
		is.addEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		is.removeEnchantment(enchantment);
		return this;
	}

	public ItemBuilder type(Material material) {
		is.setType(material);
		return this;
	}

	public ItemBuilder clearLore() {
		ItemMeta meta = is.getItemMeta();

		meta.lore(new ArrayList<>());
		is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder clearEnchantments() {
		for (Enchantment e : is.getEnchantments().keySet()) {
			is.removeEnchantment(e);
		}

		return this;
	}

	public ItemBuilder unbreakable() {
		ItemMeta im = this.is.getItemMeta();
		im.setUnbreakable(true);
		this.is.setItemMeta(im);
		return this;
	}

	public ItemBuilder customModelData(int i) {
		ItemMeta im = this.is.getItemMeta();
		im.setCustomModelData(i);
		this.is.setItemMeta(im);
		return this;
	}

	public ItemBuilder glow(boolean bool) {
		if (bool) {
			return glow();
		}
		return this;
	}

	public ItemBuilder glow() {
		is.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta im = is.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilder potionColor(Color color) {
		PotionMeta pm = (PotionMeta) is.getItemMeta();
		pm.setColor(color);
		is.setItemMeta(pm);
		return this;
	}

	public ItemBuilder trim(ArmorTrim trim) {
		ArmorMeta am = (ArmorMeta) is.getItemMeta();
		am.setTrim(trim);
		is.setItemMeta(am);
		return this;
	}

	public ItemBuilder itemFlag(ItemFlag itemFlag) {
		ItemMeta im = is.getItemMeta();
		im.addItemFlags(itemFlag);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilder skull(String owner) {
		try {
			SkullMeta im = (SkullMeta) is.getItemMeta();
			im.setOwner(owner);
			is.setItemMeta(im);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return this;
	}

	public ItemBuilder texture(String hash) {
		try {
			SkullMeta im = (SkullMeta) is.getItemMeta();
			PlayerProfile profile = Bukkit.createProfileExact(new UUID(hash.hashCode(), hash.hashCode()), "Maid-HeadTexture");
			profile.getProperties().add(new ProfileProperty("textures", hash));
			im.setPlayerProfile(profile);
			is.setItemMeta(im);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return this;
	}

	public ItemBuilder modelData(int i) {
		ItemMeta im = is.getItemMeta();
		im.setCustomModelData(i);
		is.setItemMeta(im);
		return this;
	}

	public<T, Z> ItemBuilder persistentData(NamespacedKey key, PersistentDataType<T,Z> type, Z value) {
		ItemMeta im = is.getItemMeta();
		im.getPersistentDataContainer().set(key, type, value);
		is.setItemMeta(im);
		return this;
	}

	public<T, Z> ItemBuilder persistentData(PersistentDataType<T,Z> type, Z value) {
		ItemMeta im = is.getItemMeta();
		im.getPersistentDataContainer().set(NAMESPACED_KEY_DATA, type, value);
		is.setItemMeta(im);
		return this;
	}

	public ItemStack build() {
		return build(false);
	}

	public ItemStack build(boolean clone) {
		if (!clone) {
			return is;
		}
		return is.clone();
	}
}