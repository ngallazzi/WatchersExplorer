package com.ngallazzi.watchersexplorer;

import com.ngallazzi.watchersexplorer.remote.repository.PaginationUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import okhttp3.Headers;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4ClassRunner.class)
public class PaginationUtilsTest {

    @Test
    public void getTotalPagesCountFromGihubHeaders() {
        Headers.Builder builder = new Headers.Builder();
        String paginationLink = "<https://api.github.com/repositories/10057936/subscribers?per_page=10&page=2>; " +
                "rel=\"next\", <https://api.github.com/repositories/10057936/subscribers?per_page=10&page=98>; rel=\"last\"";
        builder.add("Link", paginationLink);
        Headers header = builder.build();
        int pageCount = PaginationUtils.INSTANCE.getTotalPagesCount(header);
        assertEquals(pageCount, 98);
    }
}
