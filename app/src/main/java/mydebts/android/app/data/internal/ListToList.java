package mydebts.android.app.data.internal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;


class ListToList<T, R> implements Function<List<T>, List<R>> {
    private final Function<T, R> itemMapper;

    ListToList(Function<T, R> itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public List<R> apply(List<T> ts) throws Exception {
        if (ts == null) {
            return null;
        }

        List<R> rs = new ArrayList<>(ts.size());

        for (T t : ts) {
            rs.add(itemMapper.apply(t));
        }

        return rs;
    }
}
