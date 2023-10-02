package fr.maxlego08.zshop.zcore.utils.interfaces;

import java.util.Collection;

@FunctionalInterface
public interface CollectionConsumer<T> {

	Collection<String> accept(T t);
	
}
