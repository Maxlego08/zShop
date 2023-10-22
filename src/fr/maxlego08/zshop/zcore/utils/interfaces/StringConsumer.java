package fr.maxlego08.zshop.zcore.utils.interfaces;

@FunctionalInterface
public interface StringConsumer<T> {

	String accept(T t);
	
}
