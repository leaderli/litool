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

## lilink

## liif 

