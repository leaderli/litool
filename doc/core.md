# core

## lino

a option-like interface, have two implement `Some`,`None`. the `Some` represent the value is `present`

```java
List<Map<String,String>> map;
Object obj  = map;

Lino.of(obj)
        .cast(List.class)
        .filter(l->l.size()>1)
        .map(l->l.get(1))
        .cast(String.class,String.class)
        .map(m->m.get("a"))
        .ifPresent(e->System.out.println(e.length()))
        .ifAbsent(()->System.out.println("null"))
```

## lira

## lean

a tool to create bean from another bean, work like gson

```json
{"name":"1","bean": {"name": "2"},"beans": [{"name": "3"}]}
```

```java
class Bean<T> {
    private String name;
    private Bean bean;
    private List<T> beans;
}
```

```java
Map map = gson.fromJson(json, Map.class);
LiTypeToken<Bean<Bean>> parameterized = LiTypeToken.getParameterized(Bean.class, Bean.class);

Lean lean = new Lean();
Bean<Bean> bean = lean.fromBean(map, parameterized);
Assertions.assertEquals("3", bean.beans.get(0).name);


Bean<Bean> copy= lean.copyBean(map, parameterized);
lean.copyBean(bean, copy);
Assertions.assertEquals("3", copy.beans.get(0).name);
```

you can custom the detail of copy pojo

the progress of set pojo field is get a value from source by the fieldName, and use
suitable `TypeAdapter` to parser the value to assign to pojo field. you can custom
the find-value for source, such as

1. `LeanKey` to specific a `name`
2. register a `LeanFieldKey` at `lean`  constructor to set the `name`

```json
{"age": 1.0}
```

```java
class Bean {
    @LeanKey("age")
    private double fake;
}

Lean lean = new Lean(new LinkedHashMap<>(), Collections.singletonList(f -> {
    if (f.getName().equals("custom")) {
        return "age";
    }
    return null;
}));
```

also you can custom the value parser progress by `LeanFieldAdapter`, `lean` will check the adapter parameterType
whether suite the field type

```java

class StringTypeAdapter implements TypeAdapter<String> {

    @Override
    public String read(Object source) {
        if (source instanceof List) {
            return (String) ((List<?>) source).get(0);
        }
        return source + "";
    }
}

class ObjectTypeAdapter implements TypeAdapter<List> {

    @Override
    public List read(Object source) {
        return null;
    }
}

class Bean<T extends List> {
    @LeanFieldAdapter(StringTypeAdapter.class)
    private String name;
    @LeanFieldAdapter(ObjectTypeAdapter.class)
    private T age;
}
```

## liLink

## liIf

## BeanPath

An xpath-like tool for easy retrieval of bean properties

```java
BeanPath.simple(obj,"key")
BeanPath.parse(obj,"key.list[0].key")
// can set arr-key filter in position 
BeanPath.parse(obj,"key.list[0].list2[1].key",lino->lino.cast(String.class),lino->lino.cast(String.class))
```

