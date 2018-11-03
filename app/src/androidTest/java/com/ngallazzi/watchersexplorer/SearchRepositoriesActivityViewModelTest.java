package com.ngallazzi.watchersexplorer;

import com.ngallazzi.watchersexplorer.activities.SearchRepositoriesActivityViewModel;
import com.ngallazzi.watchersexplorer.models.RepositoriesResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.concurrent.CountDownLatch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.test.runner.AndroidJUnit4;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchRepositoriesActivityViewModelTest {
    final CountDownLatch signal = new CountDownLatch(1);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void testCall() throws Exception {
        SearchRepositoriesActivityViewModel searchRepositoriesActivityViewModel = new SearchRepositoriesActivityViewModel();
        String key = "picasso";
        final int ITEMS_PER_PAGE = 10;
        MutableLiveData<RepositoriesResponse> response = searchRepositoriesActivityViewModel.getRepositoriesResponse();
        response.observeForever(new Observer<RepositoriesResponse>() {
            @Override
            public void onChanged(RepositoriesResponse repositoriesResponse) {
                assert (repositoriesResponse.items.size() > 0);
                signal.countDown();
            }
        });
        searchRepositoriesActivityViewModel.getRepositories(key, 1, ITEMS_PER_PAGE);
        signal.await();
    }
}
