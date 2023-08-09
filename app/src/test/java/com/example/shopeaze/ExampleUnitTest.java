package com.example.shopeaze;

import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import android.text.TextUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Mock
    LoginContract.View view;
    @Mock
    LoginContract.Model model;

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }   //works

    @Test
    public void nullViewObject(){               //works
        assertEquals(view.getEmail(), null);
    }
    @Test
    public void checkEmptyUsername(){           //loginUser in Presenter class
        //when(view.getUsername()).thenReturn("abc");     //when getUsername of view is invoked, return abc
        //when(model.isFound("abc")).thenReturn(true);
        //Presenter presenter = new Presenter(model, view);
        //presenter.checkUsername();
        //verify(view).displayMessage("user found");


//        when(view.getEmail()).thenReturn("");
//        when(view.getPassword()).thenReturn("");
        //LoginContract.Presenter presenter = new LoginPresenter(view);
//        presenter.loginUser();
 //       verify(view).showErrorMessage("Please enter email");

    }
}