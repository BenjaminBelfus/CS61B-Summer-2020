import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.Objects;

public class DBTable<T> {
    protected List<T> entries;

    public DBTable() {
        this.entries = new ArrayList<>();
    }

    public DBTable(Collection<T> lst) {
        entries = new ArrayList<>(lst);
    }

    public void add(T t) {
        entries.add(t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DBTable<?> other = (DBTable<?>) o;
        return Objects.equals(entries, other.entries);
    }

    /** Add all items from a collection to the table. */
    public void add(Collection<T> col) {
        col.forEach(this::add);
    }

    /** Returns a copy of the entries in this table. */
    List<T> getEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * getOrderedBy should create a new list ordered on the results of the
     * getter, without modifying the entries.
     */
    public <R extends Comparable<R>> List<T> getOrderedBy(Function<T, R> getter) {
        // TODO
        List<T> saved = this.getEntries();
        saved.sort((o1, o2) -> getter.apply(o1).compareTo(getter.apply(o2)));
        //this.entries.sort((o1, o2) -> getter.apply(o1).compareTo(getter.apply(o2)));
        return saved;
    }

    public static void main(String[] args) {
        List<User> users = Arrays.asList(
                new User(2, "Matt", ""),
                new User(4, "Zoe", ""),
                new User(5, "Alex", ""),
                new User(1, "Shreya", ""),
                new User(1, "Connor", "")
        );
        DBTable<User> t = new DBTable<>(users);
        //List<User> l = t.getOrderedBy(User::getName);
        List<User> l = t.getOrderedBy(User::getId);
        l.forEach(System.out::println);
    }
}