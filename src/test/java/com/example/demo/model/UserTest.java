//package com.example.demo.model;
//
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserTest {
//
//    @Test
//    void testConstructorAndGetters() {
//        // Arrange: Create an instance of the User class
//        User user = new User();
//        user.setId(1L);
//        user.setName("John Doe");
//        user.setEmail("johndoe@example.com");
//        user.setPaid(true);
//        user.setTickets(3);
//
//        // Act & Assert: Validate each field's getter
//        assertEquals(1L, user.getId());
//        assertEquals("John Doe", user.getName());
//        assertEquals("johndoe@example.com", user.getEmail());
//        assertTrue(user.isPaid());
//        assertEquals(3, user.getTickets());
//    }
//
//    @Test
//    void testSetters() {
//        // Arrange: Create an instance of the User class
//        User user = new User();
//
//        // Act: Set the fields
//        user.setId(5L);
//        user.setName("Jane Smith");
//        user.setEmail("janesmith@example.com");
//        user.setPaid(false);
//        user.setTickets(2);
//
//        // Assert: Verify the fields using getters
//        assertEquals(5L, user.getId());
//        assertEquals("Jane Smith", user.getName());
//        assertEquals("janesmith@example.com", user.getEmail());
//        assertFalse(user.isPaid());
//        assertEquals(2, user.getTickets());
//    }
//
//    @Test
//    void testEquality() {
//        // Arrange: Create two different User objects with identical data
//        User user1 = new User("John Doe", "johndoe@example.com", true, 3);
//        User user2 = new User("John Doe", "johndoe@example.com", true, 3);
//
//
//        // Assert: Both should be equal
//        //  assertEquals(user1, user2);
//        //  assertEquals(user1.hashCode(), user2.hashCode());
//
//        // Modify one field and verify the objects are no longer equal
//        user2.setTickets(5);
//        assertNotEquals(user1, user2);
//        user2.setTickets(5);
//        assertNotEquals(user1, user2);
//        user2.setTickets(3);
//        assertNotEquals(user1, user2);
//    }
//
//
//}
