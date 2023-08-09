package com.example.shopeaze;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import android.text.TextUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;

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
    public void checkEmptyUsername(){
        //when(view.getUsername()).thenReturn("abc");     //when getUsername of view is invoked, return abc
        //when(model.isFound("abc")).thenReturn(true);
        //Presenter presenter = new Presenter(model, view);
        //presenter.checkUsername();
        //verify(view).displayMessage("user found");

        when(view.getEmail()).thenReturn("");
        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).showErrorMessage("Please enter email");

    }

    @Test
    public void checkEmptyPassword(){
        when(view.getEmail()).thenReturn("anyemail@email.com");
        when(view.getPassword()).thenReturn("");
        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).showErrorMessage("Please enter password");

    }
    @Test
    public void emptyEmailOrder(){
        when(view.getEmail()).thenReturn("");
        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        InOrder order = inOrder(view);
        order.verify(view).getEmail();
        order.verify(view).showErrorMessage("Please enter email");
    }
}