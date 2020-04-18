package de.TheHolyException.suro.utils;

import java.util.UUID;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.DataMutateResult;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.context.ContextManager;

public class LuckPermsUtils {
	
	private static LuckPermsApi api = LuckPerms.getApi();

	private static ExpiringMap<UUID, MetaData> data = new ExpiringMap<UUID, MetaData>(20, 2);
	
	public static MetaData getMetaData(UUID uuid) {
		User user = api.getUser(uuid);
		ContextManager manager = api.getContextManager();
		MetaData metadata = user.getCachedData().getMetaData(manager.lookupApplicableContexts(user).orElse(manager.getStaticContexts()));
		data.put(uuid, metadata);
		return metadata;
	}
	
	public static String getGroup(UUID uuid) {
		User user = api.getUser(uuid);
		return user.getPrimaryGroup();
	}
	
	public static DataMutateResult setGroup(UUID uuid, String group) {
		User user = api.getUser(uuid);
		DataMutateResult result = user.setPrimaryGroup(group);
		api.getUserManager().saveUser(user);
		return result;
	}

}
