package com.example.shopeaze;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void checkEmptyUsername(){
        LoginContract.Presenter presenter = new LoginPresenter(view);
        presenter.loginUser("", "");
        verify(view).showErrorMessage("Please enter email");
    }

}