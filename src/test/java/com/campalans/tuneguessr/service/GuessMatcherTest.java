package com.campalans.tuneguessr.service;

import org.junit.jupiter.api.Test;

public class GuessMatcherTest {

    private final GuessMatcher guessMatcher = new GuessMatcher();

    @Test
    void guess_is_correct_when_equals_ignore_case() {
        assert(guessMatcher.isGuessCorrect("Hello", "hello"));
        assert(guessMatcher.isGuessCorrect("HELLO", "hello"));
        assert(guessMatcher.isGuessCorrect("hello", "hello"));
    }

    @Test
    void guess_is_correct_when_equals_ignore_trim() {
        assert(guessMatcher.isGuessCorrect("  Hello  ", "hello"));
        assert(guessMatcher.isGuessCorrect("HELLO", " hello "));
        assert(guessMatcher.isGuessCorrect("hello", "hello"));
    }

    @Test
    void guess_is_correct_when_equals_ignore_accents() {
        assert(guessMatcher.isGuessCorrect("Héllo", "hello"));
        assert(guessMatcher.isGuessCorrect("HELLO", "héllo"));
        assert(guessMatcher.isGuessCorrect("hello", "hello"));
    }
}
