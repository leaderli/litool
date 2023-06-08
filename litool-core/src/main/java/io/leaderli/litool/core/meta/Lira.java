package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.collection.Generators;
import io.leaderli.litool.core.collection.IterableItr;
import io.leaderli.litool.core.collection.NoneItr;
import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.lang.CompareDecorator;
import io.leaderli.litool.core.lang.EqualComparator;
import io.leaderli.litool.core.meta.ra.*;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;


/**
 * Lira是一个集合，提供了一些方便的方法，这些操作只有在执行终结操作时才会执行。
 * <p>
 * lira不应该使用包含lira的工具类，例如{@link io.leaderli.litool.core.bit.BitStr}
 * <p>
 * Lira链将捕获所有异常并执行{@link Exceptionable#onError(Throwable, CancelSubscription)}操作，您可以在{@link #onError(Exceptionable)}上监听异常，并重新抛出异常以通过RuntimeException中断链，或者通过{@link CancelSubscription#cancel()}取消链。
 * 为了方便起见，您可以使用{@link #assertNoError()}或{@link #assertTrue(Function)}手动使用RuntimeException中断链。
 * 或者抛出一个特定的{@link LiraRuntimeException}。
 * <p>
 * 大多数终端操作将删除空元素，如果需要保留则可以使用nullable系列的方法
 *
 * @param <T> 集合元素类型
 * @author leaderli
 * @since 2022/6/19
 */
public interface Lira<T> extends LiValue, PublisherRa<T>, Iterable<T> {


    /**
     * 空集合，所有的空集合共享该实例
     */
    Lira<?> NONE_INSTANCE = new IterableRa<>(NoneItr.of());

    /**
     * 将具有通配符类型的Lira转换为具体类型的Lira
     *
     * @param lira 具有通配符类型的Lira
     * @param <T>  Lira中元素的类型
     * @return 具体类型的Lira
     */
    @SuppressWarnings("unchecked")
    static <T> Lira<T> narrow(Lira<? extends T> lira) {
        return (Lira<T>) lira;
    }

    /**
     * 通过可变参数构造Lira
     *
     * @param elements 可变参数列表
     * @param <T>      Lira中元素的类型
     * @return 新构造的Lira
     */
    @SafeVarargs
    static <T> Lira<T> of(T... elements) {
        return of(IterableItr.ofs(elements));
    }

    /**
     * 通过IterableItr构造Lira
     *
     * @param iterableItr IterableItr类型
     * @param <T>         Lira中元素的类型
     * @return 新构造的Lira
     */
    static <T> Lira<T> of(IterableItr<? extends T> iterableItr) {
        if (iterableItr != null && iterableItr.hasNext()) {
            return new IterableRa<>(iterableItr);
        }
        return none();
    }

    /**
     * 返回一个不包含任何元素的Lira
     *
     * @param <T> Lira中元素的类型
     * @return 唯一的空Lira
     * @see #NONE_INSTANCE
     */
    @SuppressWarnings("unchecked")
    static <T> Lira<T> none() {
        return (Lira<T>) NONE_INSTANCE;
    }

    /**
     * 通过Enumeration构造Lira
     *
     * @param enumeration 枚举类型
     * @param <T>         Lira中元素的类型
     * @return 新构造的Lira
     */
    static <T> Lira<T> of(Enumeration<? extends T> enumeration) {
        return of(IterableItr.of(enumeration));
    }

    /**
     * 通过Stream构造Lira
     *
     * @param stream Stream类型
     * @param <T>    Lira中元素的类型
     * @return 新构造的Lira
     */
    static <T> Lira<T> of(Stream<? extends T> stream) {
        return of(IterableItr.of(stream));
    }

    /**
     * 通过Object构造Lira
     *
     * @param obj 对象类型
     * @param <T> Lira中元素的类型
     * @return 新构造的Lira
     * @see IterableItr#of(Object)
     */
    static <T> Lira<T> iterableItr(Object obj) {
        return of(IterableItr.of(obj));
    }

    /**
     * 返回一个包含自增整数的无限Lira，起始值为0
     *
     * @return 自增整数的无限Lira
     * @see Generators#range()
     */
    static Lira<Integer> range() {
        return Lira.of(Generators.range());
    }

    /**
     * 通过Iterator构造Lira
     *
     * @param iterator 迭代器类型
     * @param <T>      Lira中元素的类型
     * @return 新构造的Lira
     */
    static <T> Lira<T> of(Iterator<? extends T> iterator) {
        return of(IterableItr.of(iterator));
    }

    /**
     * 通过Iterable构造Lira
     *
     * @param iterable Iterable类型
     * @param <T>      Lira中元素的类型
     * @return 新构造的Lira
     */
    static <T> Lira<T> of(Iterable<? extends T> iterable) {
        if (iterable == null || !iterable.iterator().hasNext()) {
            return none();
        }
        return new IterableRa<>(iterable);
    }

    /**
     * 过滤可以强制转换的元素，如果元素为null，则保留它
     * <p>
     * typeToken是用于通用目的的，实际转换类是{@link LiTypeToken#getRawType()}
     *
     * @param typeToken 要转换的类的typeToken
     * @param <R>       typeToken的泛型参数
     * @return 返回过滤后的Lira
     * @see #cast(Class)
     */
    @SuppressWarnings("unchecked")
    default <R> Lira<R> cast(LiTypeToken<? extends R> typeToken) {
        return (Lira<R>) cast(typeToken.getRawType());
    }

    /**
     * 过滤可以强制转换的元素，如果元素为null，则保留它
     *
     * @param type 元素所代表的类或接口
     * @param <R>  type的泛型参数
     * @return 返回过滤后的Lira
     */
    <R> Lira<R> cast(Class<? extends R> type);

    /**
     * 过滤可以强制转换为Map的元素，以及Map的键、值可以强制转换的元素。如果元素为null，则保留它
     *
     * @param <K>       转换后的Map键类型的泛型参数
     * @param <V>       转换后的Map值类型的泛型参数
     * @param keyType   转换后的Map的键类型
     * @param valueType 转换后的Map的值类型
     * @return 返回过滤后的新Lira
     */
    <K, V> Lira<Map<K, V>> cast(Class<? extends K> keyType, Class<? extends V> valueType);

    /**
     * 返回此Lira是否包含指定元素
     * <p>
     * 这是一个终端操作
     *
     * @param t 要测试其是否在此Lira中存在的元素
     * @return 如果此Lira包含指定元素，则返回true
     */
    boolean contains(T t);

    /**
     * 删除与给定元素相等的元素
     *
     * @param t 要删除的元素
     * @return 返回一个新的Lira
     * @see #filter(Function)
     * @see #filter_null()
     */
    default Lira<T> remove(T t) {

        if (t == null) {
            return filter_null();
        }
        return filter(v -> !t.equals(v));
    }

    /**
     * 过滤Lira中的元素，如果Function.apply(Object)的结果通过BooleanUtil.parse(Boolean)解析为true，则保留该元素
     * <p>
     * 默认情况下，null元素将被删除，除非过滤器是NullableFunction的实例
     *
     * @param filter 过滤函数
     * @return 返回一个新的Lira
     * @see BooleanUtil#parse(Object)
     */
    Lira<T> filter(Function<? super T, ?> filter);

    /**
     * 删除null元素
     *
     * @return 返回一个新的Lira
     * @see #filter(Function)
     */
    Lira<T> filter_null();

    /**
     * 如果过滤后的Lira存在，则返回第一个元素的Lino，否则返回{@link Lino#none()}
     * <pre>
     *    return  filter(filter).first()
     * </pre>
     * <p>
     *
     * @param filter 过滤函数
     *               这是一个终端操作
     * @return Lira中的第一个元素
     */
    default Lino<T> first(Function<? super T, ?> filter) {
        return filter(filter).first();
    }

    /**
     * 如果Lira存在，则返回第一个元素的Lino，否则返回{@link Lino#none()}
     * <p>
     * 这是一个终端操作
     *
     * @return Lira中的第一个元素
     */
    Lino<T> first();

    /**
     * 如果在过滤后的Lira中存在，则返回最后一个元素的Lino，否则返回{@link Lino#none()}
     * <p>
     * 这是一个终端操作
     *
     * <pre>
     *    return  filter(filter).last()
     * </pre>
     *
     * @param filter 过滤函数
     * @return Lira中的最后一个元素
     */
    default Lino<T> last(Function<? super T, ?> filter) {
        return filter(filter).last();
    }

    /**
     * 如果Lira中存在，则返回最后一个元素的Lino，否则返回{@link Lino#none()}
     * <p>
     * 这是一个终端操作
     *
     * @return Lira中的最后一个元素
     */
    Lino<T> last();

    /**
     * 获取指定索引位置上的元素对应的行号
     *
     * @param index 元素的索引
     * @return 返回该元素对应的行号{@link Lino}，如果元素不存在，则返回{@link Lino#none()}
     */
    Lino<T> get(int index);

    /**
     * 终端操作，返回一个迭代器，该迭代器已经移除了所有值为{@code null}的元素
     *
     * @return 迭代器
     */
    @Override
    Iterator<T> iterator();

    /**
     * 终端操作，对每个元素执行指定操作
     *
     * @param consumer 对每个元素执行的操作
     */
    @Override
    void forEach(Consumer<? super T> consumer);

    /**
     * 终端操作，对每个可能包含{@code null}的元素执行指定操作
     *
     * @param consumer 对每个元素执行的操作
     */
    void forNullableEach(Consumer<? super T> consumer);

    /**
     * 终端操作，返回一个迭代器，该迭代器可能包含{@code null}的元素
     *
     * @return 迭代器
     */
    Iterator<T> nullableIterator();

    /**
     * @param replacement 当元素为null时，会被替换为该值
     * @return 新的Lira对象
     */
    default Lira<T> nullable(T replacement) {
        return nullable(() -> replacement);
    }

    /**
     * 如果元素为null，则由提供程序将其转换为元素。如果元素不为null，则只需传递自身
     *
     * @param supplier 提供一个新元素的供应商
     * @return 如果调用{@link io.leaderli.litool.core.meta.ra.SubscriberRa#next_null()}，则返回结果
     */
    Lira<T> nullable(Supplier<? extends T> supplier);


    /**
     * 限制元素数量不超过max个，注意Generator会跳过limit之后的元素。空元素会被计算为有效限制元素，
     * 但终止操作会丢弃空元素。例如：
     * <pre>
     *     Lira.of(1,null,2,3).limit(3).size() == 2
     * </pre>
     * 为了避免这种情况，可以这样编码：
     * <pre>
     *     Lira.of(1,null,2,3).filter_null().limit(3).size() == 3
     * </pre>
     *
     * @param max 最大元素数量限制
     * @return 新的Lira对象
     */
    Lira<T> limit(int max);

    /**
     * 跳过前min个元素，只接受min之后的元素
     *
     * @param min 要跳过的第一个元素的索引
     * @return 新的Lira对象
     */
    Lira<T> skip(int min);

    /**
     * 消费元素直到满足过滤器条件
     *
     * @param filter 过滤器
     * @return 新的Lira对象
     * @see BooleanUtil#parse(Object)
     */
    Lira<T> takeWhile(Function<? super T, ?> filter);

    /**
     * 跳过元素直到满足过滤器条件
     *
     * @param filter 过滤器
     * @return 新的Lira对象
     * @see BooleanUtil#parse(Object)
     */
    Lira<T> dropWhile(Function<? super T, ?> filter);


    /**
     * 将元素扁平化为Lira对象
     *
     * @param <R> 扁平化后的Lira对象类型
     * @return 新的Lira对象
     * @see #flatMap(Function)
     */
    <R> Lira<R> flatMap();

    /**
     * 将元素通过映射器扁平化为Lira对象
     *
     * @param <R>    扁平化后的Lira对象类型
     * @param mapper 映射器
     * @return 新的Lira对象
     */
    <R> Lira<R> flatMap(Function<? super T, Iterator<? extends R>> mapper);


    /**
     * 对元素进行unzip操作，返回一个包含泛型R类型元素的新的Lira
     * <pre>
     *     return map(mapper).map(Supplier::get)
     * </pre>
     * *
     *
     * @param mapper 一个函数，接受元素并返回包含R类型元素的Supplier
     * @param <R>    新Lira中元素的类型
     * @return 返回一个新的R类型的Lira对象
     */
    default <R> Lira<R> unzip(Function<? super T, Supplier<? extends R>> mapper) {
        return map(mapper).map(Supplier::get);
    }

    /**
     * 对元素进行map操作，返回一个包含泛型R类型元素的新的Lira
     *
     * @param mapper 一个函数，接受元素并返回泛型R类型的元素
     * @param <R>    新Lira中元素的类型
     * @return 返回一个新的Lira对象
     */
    <R> Lira<R> map(Function<? super T, ? extends R> mapper);


    /**
     * 当发生异常时，执行{@link SubscriberRa#next_null()}和
     * {@link SubscriberRa#onError(Throwable, CancelSubscription)}
     *
     * @param mapper 一个函数，接受元素并返回泛型R类型的元素
     * @param <R>    新Lira中元素的类型
     * @return 返回一个新的Lira对象
     * @see #throwable_map(ThrowableFunction, Consumer)
     * @see WhenThrowBehavior#WHEN_THROW
     */
    <R> Lira<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapper);

    /**
     * 当发生异常时，执行{@link SubscriberRa#next_null()}和
     * {@link SubscriberRa#onError(Throwable, CancelSubscription)}
     *
     * @param mapper    一个函数，接受元素并返回泛型R类型的元素
     * @param <R>       新Lira中元素的类型
     * @param whenThrow 当{@link ThrowableFunction#apply(Object)}抛出异常时执行的消费者
     * @return 返回一个新的Lira对象
     */
    <R> Lira<R> throwable_map(ThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow);

    /**
     * 终端操作，触发执行先前的元素
     *
     * @param deliverAction 对缓存列表执行的消费者
     * @return 返回一个新的Lira对象
     * @see #terminalMap(Function)
     */
    default Lira<T> terminal(Consumer<List<T>> deliverAction) {
        return terminalMap(list -> {
            deliverAction.accept(list);
            return list;
        });
    }

    /**
     * 终端操作，触发Lira执行，并将元素存储在列表中
     *
     * @param deliverAction 对缓存列表执行的消费者
     * @return 返回一个新的Lira对象
     */
    Lira<T> terminalMap(Function<List<T>, Iterable<T>> deliverAction);

    /**
     * 断言
     *
     * @param filter 断言函数
     * @return 新的Lira对象
     */
    default Lira<T> assertTrue(Function<? super T, ?> filter) {
        return assertTrue(filter, "");
    }

    /**
     * 断言
     *
     * @param filter 断言函数
     * @param errMsg 断言失败后的异常消息
     * @return 新的Lira对象
     */
    default Lira<T> assertTrue(Function<? super T, ?> filter, String errMsg) {
        return assertTrue(filter, () -> errMsg);
    }

    /**
     * 断言
     *
     * @param filter 断言函数
     * @param errMsg 断言失败后的异常消息
     * @return 新的Lira对象
     */
    default Lira<T> assertTrue(Function<? super T, ?> filter, Supplier<String> errMsg) {
        return assertTrue(filter, t -> errMsg.get());
    }

    /**
     * 断言
     *
     * @param filter      断言函数
     * @param errFunction 断言失败后的异常函数
     * @return 新的Lira对象
     */
    default Lira<T> assertTrue(Function<? super T, ?> filter, Function<? super T, String> errFunction) {
        return debug(new DebugConsumer<T>() {

            @Override
            public void accept(T e) {
                if (!BooleanUtil.parse(filter.apply(e))) {
                    throw new LiraRuntimeException("assert fail of " + filter + " : " + errFunction.apply(e));
                }
            }

            @Override
            public void onNull() {
                throw new LiraRuntimeException("assert fail of " + filter);
            }
        });
    }

    /**
     * 遍历此Lira中的每个元素，并对元素执行某个操作
     *
     * @param action 对元素执行的操作
     * @return 返回当前对象
     */
    Lira<T> debug(DebugConsumer<T> action);

    /**
     * 断言执行链中未抛出过异常
     *
     * @return 新的Lira对象
     */
    default Lira<T> assertNoError() {
        return onError((t, c) -> {
            throw new IllegalStateException(t);
        });
    }

    /**
     * 如果在对元素执行操作时，此Lira抛出了异常，则不会调用{@link SubscriberRa#next(Object)}，
     * 而是会调用 {@link  SubscriberRa#next_null()}和{@link  SubscriberRa#onError(Throwable, CancelSubscription)}.方法，
     * 最终会将异常传递给{@link  Exceptionable#onError(Throwable, CancelSubscription)}
     *
     * @param onError 处理异常的方法
     * @return 返回一个新的Lira对象
     */
    Lira<T> onError(Exceptionable onError);

    /**
     * 当元素为null是，返回左值，不为null时返回右值
     *
     * @param l   left值
     * @param <L> left值的类型
     * @return Either对象
     */
    default <L> Lira<Either<L, T>> either(L l) {
        return eitherSupplier(() -> l);
    }

    /**
     * 该方法用于提供一个left值或者获取T类型值，返回一个Either对象
     *
     * @param l   left值提供者
     * @param <L> left值的类型
     * @return Either对象
     */
    <L> Lira<Either<L, T>> eitherSupplier(Supplier<? extends L> l);

    /**
     * 遍历此Lira中的每个元素，并对元素执行{@code System.out.println(element)}
     *
     * @return 返回当前对象
     */
    Lira<T> debug();

    /**
     * or方法为终端操作，将调用{@link #present()}方法判断元素是否存在于此处。
     * 如果存在，则返回当前Lira对象，否则返回一个包含传入参数的{@link #of(Object[])}对象。
     *
     * @param alternate 传入的备选元素数组
     * @return 返回一个带有备选元素的Lira对象
     */
    @SuppressWarnings("unchecked")
    Lira<T> or(T... alternate);

    /**
     * or方法为终端操作，将调用{@link #present()}方法判断元素是否存在于此处。
     * 如果存在，则返回当前Lira对象，否则返回一个包含传入参数的{@link #of(Iterator)}对象。
     *
     * @param alternate 传入的备选元素迭代器
     * @return 返回一个带有备选元素的Lira对象
     */
    Lira<T> or(Iterator<? extends T> alternate);

    /**
     * or方法为终端操作，将调用{@link #present()}方法判断元素是否存在于此处。
     * 如果存在，则返回当前Lira对象，否则返回一个包含传入参数的{@link #of(Iterable)}对象。
     *
     * @param alternate 传入的备选元素可迭代对象
     * @return 返回一个带有备选元素的Lira对象
     */
    Lira<T> or(Iterable<? extends T> alternate);

    /**
     * 通过累加器函数对此Lira的元素进行归约，并返回归约后的结果。
     * <pre> {@code
     *     T result =  null
     *     for (T element : this stream){
     *          if(result == null){
     *              result = element;
     *          }else{
     *             result = accumulator.apply(result, element)
     *             if(result == null){
     *                 return null;
     *             }
     *          }
     *     }
     *     return result;
     * }</pre>*
     *
     * @param accumulator 一个用于组合两个值的关联非干扰状态函数
     * @return 归约后的结果
     */
    Lino<T> reduce(BinaryOperator<T> accumulator);


    /**
     * 通过累加器函数对此Lira的元素进行归约，并返回归约后的结果。
     * <pre> {@code
     *     T result =  identify
     *     for (T element : this stream){
     *             result = accumulator.apply(result, element)
     *             if(result == null){
     *                 return null;
     *             }
     *     }
     *     return result;
     * }</pre>*
     *
     * @param identity    累加函数的标识值
     * @param accumulator 一个用于组合两个值的关联非干扰状态函数
     * @return 归约后的结果
     */
    Lino<T> reduce(T identity, BinaryOperator<T> accumulator);

    /**
     * 终结操作，返回此列表中的元素数。
     *
     * @return 列表中的元素数
     */
    int size();


    /**
     * 等待一段时间。
     *
     * @param milliseconds 等待时间（以毫秒为单位）
     * @return 新的Lira
     */
    default Lira<T> sleep(long milliseconds) {
        return sleep(1, milliseconds);
    }

    /**
     * 每隔n次操作，等待一段时间。
     *
     * @param countdown    计数器
     * @param milliseconds 等待时间（以毫秒为单位）
     * @return 新的Lira
     */
    Lira<T> sleep(int countdown, long milliseconds);

    /**
     * 去除重复元素。
     *
     * @return 新的Lira
     */
    Lira<T> distinct();


    /**
     * 返回一个包含了不重复元素的新 Lira。
     *
     * <p>根据传入的 {@code EqualComparator} 进行元素比较。
     * 这是一个中间操作。
     *
     * @param comparator 用于比较元素是否相等的 {@code EqualComparator}
     * @return 包含了不重复元素的新 Lira
     * @see #terminalMap(Function)
     * @see CompareDecorator
     */
    Lira<T> distinct(EqualComparator<? super T> comparator);

    /**
     * 使用默认的 {@link Comparator} 进行排序，即 {@code {sorted(null)}}。
     * <p>
     * 这是一个中间操作。
     *
     * @return 排序后的新 Lira
     * @see #terminalMap(Function)
     */
    default Lira<T> sorted() {
        return sorted(null);
    }


    /**
     * 返回一个按照传入的 {@code Comparator} 进行排序后的新 Lira。
     * 这是一个中间操作。
     *
     * @param comparator 用于比较元素大小的 {@code Comparator}
     * @return 排序后的新 Lira
     * @see #terminalMap(Function)
     */
    Lira<T> sorted(Comparator<? super T> comparator);

    /**
     * 对 Lira 中的每个元素执行传入的 {@code ThrowableConsumer} 操作。
     * 当出现异常时，使用默认的异常处理器 {@link WhenThrowBehavior#WHEN_THROW} 进行处理。
     * 这是一个终止操作。
     *
     * @param action 可能会出现异常的操作
     */
    void forThrowableEach(ThrowableConsumer<? super T> action);


    /**
     * 对 Lira 中的每个元素执行传入的 {@code ThrowableConsumer} 操作。
     * <p>
     * 当出现异常时，使用传入的 {@code Consumer<Throwable>} 进行处理。
     * <p>
     * 这是一个终止操作。
     *
     * @param action    可能会出现异常的操作
     * @param whenThrow 异常处理器
     */
    void forThrowableEach(ThrowableConsumer<? super T> action, Consumer<Throwable> whenThrow);

    /**
     * 返回一个 {@link Stream} 对象，可以对 Lira 中的元素进行更多操作。
     * <p>
     * 这是一个终止操作。
     *
     * @return 新的 Stream 对象
     * @see Stream
     */
    default Stream<T> stream() {
        return get().stream();
    }

    /**
     * 返回一个由本 Lira 中所有非空元素组成的列表
     *
     * <p>这是一个终止操作
     *
     * @return 由本 Lira 中所有非空元素组成的列表
     */
    List<T> get();

    /**
     * 返回一个包含本 Lira 中所有满足指定类型的元素的数组，如果不满足类型要求，则会被过滤掉
     * <pre>
     *     cast(type).get().toArray(new T[0])
     * </pre>*
     * <p>这是一个可空的终止操作
     *
     * @param type 数组类型
     * @param <R>  数组元素类型
     * @return 一个包含本 Lira 中所有满足指定类型的元素的数组
     */
    default <R> R[] toArray(Class<R> type) {

        Object[] original = cast(type).get().toArray();

        R[] copy = ClassUtil.newWrapperArray(type, original.length);
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    /**
     * 返回一个包含本 Lira 中所有满足指定类型的元素的数组，如果不满足类型要求，则会被过滤掉
     *
     * <pre>
     *     cast(type).nullableGet().toArray(new T[0])
     * </pre>*
     * <p>这是一个可空的终止操作
     *
     * @param type 数组类型
     * @param <R>  数组元素类型
     * @return 一个包含本 Lira 中所有满足指定类型的元素的数组
     */
    default <R> R[] toNullableArray(Class<R> type) {

        Object[] original = cast(type).nullableGet().toArray();
        R[] copy = ClassUtil.newWrapperArray(type, original.length);
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    /**
     * 返回一个由本 Lira 中所有元素组成的列表，包括空元素
     *
     * <p>这是一个终止操作
     *
     * @return 由本 Lira 中所有元素组成的列表，包括空元素
     */
    List<T> nullableGet();


    /**
     * 根据元素和转换函数生成的新的元祖lira
     *
     * @param mapper 转换函数
     * @param <R>    元祖的第二个值的类型
     * @return 一个由元祖组成的新 lira
     */
    <R> Lira<LiTuple<T, R>> tuple(Function<? super T, ? extends R> mapper);


    /**
     * 使用给定的函数将此流的元素应用于结果的映射，使用 {@code keyMapper.apply(element), valueMapper.apply(element)}作为key和value
     * <p> the {@code key == null } entry will be  removed
     * <p>这是一个终止操作
     *
     * @param keyMapper   应用于每个元素的无状态函数
     * @param valueMapper 应用于每个元素的无状态函数
     * @param <K>         Map键的类型
     * @param <V>         Map值的类型
     * @return 新的Map
     */
    <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper);

    /**
     * 使用给定的函数将此流的元素应用于结果的映射，使用{@code LiTuple2._1, LiTuple2._2} 作为key和value
     * <p> the {@code key == null } entry will be  removed
     * <p>这是一个终止操作
     *
     * @param mapper 应用于每个元素的无状态函数
     * @param <K>    Map键的类型
     * @param <V>    Map值的类型
     * @return 新的Map
     */
    <K, V> Map<K, V> toMap(Function<? super T, LiTuple<? extends K, ? extends V>> mapper);


}
