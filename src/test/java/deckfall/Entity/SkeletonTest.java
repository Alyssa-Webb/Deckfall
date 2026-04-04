package deckfall.Entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SkeletonTest {
    @Test
    void testDefaultConstructor() {
        Skeleton defaultSkeleton = new Skeleton();
        assertEquals("Skeleton", defaultSkeleton.getName());
        assertEquals(10, defaultSkeleton.getHP());
    }

}
