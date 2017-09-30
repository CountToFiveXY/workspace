package com.jason.app.objects.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.Mock;

import com.jason.app.objects.Person;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PersonTest {

	@Mock Person person;

	@Before
    public void setUp() throws Exception {
        person = mock(Person.class);
    }
}
