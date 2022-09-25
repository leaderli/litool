package io.leaderli.litool.core.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

/**
 * @author leaderli
 * @since 2022/9/25 11:21 AM
 */
class EitherTest {

    @Test
    void either() {

        Either<Integer, Integer> either = Either.left(1);
        Assertions.assertEquals(1, either.getLeftLino().get());
        Assertions.assertTrue(either.getLino().absent());


        either = Either.right(2);
        Assertions.assertEquals(2, either.getLino().get());
        Assertions.assertTrue(either.getLeftLino().absent());

        either = either.swap();
        Assertions.assertEquals(2, either.getLeftLino().get());
        Assertions.assertTrue(either.getLino().absent());
    }

    @Test
    void tuple2() {

        LiTuple2<Integer, Integer> either = LiTuple.of(null, null);

        Assertions.assertTrue(either.isRight());
        LiTuple2<Integer, Integer> finalEither = either;
        Assertions.assertThrows(NoSuchElementException.class, finalEither::getLeft);
        Assertions.assertFalse(either.isLeft());

        either = either.update2(1);
        Assertions.assertTrue(either.isRight());
        Assertions.assertFalse(either.isLeft());

        either = either.update1(1);
        Assertions.assertTrue(either.isRight());
        Assertions.assertFalse(either.isLeft());

        either = either.update2(null);
        Assertions.assertFalse(either.isRight());
        Assertions.assertTrue(either.isLeft());
    }
}
