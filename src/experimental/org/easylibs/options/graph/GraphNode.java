package org.easylibs.options.graph;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.easylibs.experimental.ComplexOption;
import org.easylibs.experimental.Group;
import org.easylibs.experimental.NoSuchOptionException;
import org.easylibs.options.TypeRegistry;
import org.easylibs.options.dialect.Dialect;

public class GraphNode extends BranchNode implements Graph {

	private final LookupVisitor lookup = new LookupVisitor(this);
	private final TypeRegistry registry = TypeRegistry.global();

	public GraphNode() {
		super("root");
	}

	public GraphNode(String name) {
		super(name);
	}

	@Override
	public TypeRegistry registry() {
		return registry;
	}

	@Override
	public String toString() {
		return "Graph [name='" + name() + "']";
	}

	@Override
	public synchronized <T extends LeafNode> T addNode(T child) {
		super.addNode(child);

		child.onAttach();

		return child;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <N> Optional<N> findDown(Class<N> type, Predicate<N> filter) {

		if (type == TypeRegistry.class) {
			return Optional.of((N) registry);
		}

		Optional<N> node = super.findDown(type, filter);

		if (!node.isPresent() && ((type == Dialect.class) || (type == DialectNode.class))) {
			addNode(new DialectNode("posix", null));

			return super.findDown(type, filter);
		}

		return node;
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Group add(Group parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRoot(Group group) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Group findGroup(String name) throws NoSuchOptionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph group(String name, Consumer<Group> consumer) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> Optional<ComplexOption<T>> findOption(String name, Class<T> type) {
		return (Optional) lookup.option(name);
	}

	@Override
	public Optional<ComplexOption<String>> findOption(String name) {
		return findOption(name, String.class);
	}

	@Override
	public <T> ComplexOption<T> getOption(String name, Class<T> type) throws NoSuchOptionException {
		return findOption(name, type)
				.orElseThrow(NoSuchOptionException::new);
	}

	@Override
	public ComplexOption<String> getOption(String name) throws NoSuchOptionException {
		return findOption(name, String.class)
				.orElseThrow(NoSuchOptionException::new);
	}

	public TypeRegistry getRegistry() {
		return registry;
	}

	@Override
	public void visit(GraphVisitor visitor) {
		visitor.visit(this);
	}

}
