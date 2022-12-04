/*
 *  /**
 *  * Copyright (C) 2017  Grbl Controller Contributors
 *  *
 *  * This program is free software; you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation; either version 2 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License along
 *  * with this program; if not, write to the Free Software Foundation, Inc.,
 *  * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *  * <http://www.gnu.org/licenses/>
 *
 */

package com.arturmaslov.miniplazma;

import androidx.appcompat.app.AppCompatDelegate;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.orm.SugarApp;

import com.arturmaslov.miniplazma.network.GoRestService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GrblController extends SugarApp {

    private final String TAG = GrblController.class.getSimpleName();
    private static GrblController grblController;
    private GoRestService goRestService;

    @Override
    public void onCreate() {
        super.onCreate();

        configureCrashReporting();

        grblController = this;

//        Picasso.Builder builder = new Picasso.Builder(this);
//        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
//        Picasso picasso = builder.build();
//        picasso.setIndicatorsEnabled(BuildConfig.DEBUG);
//        picasso.setLoggingEnabled(BuildConfig.DEBUG);
//        Picasso.setSingletonInstance(picasso);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Iconify.with(new FontAwesomeModule());
    }

    public static synchronized GrblController getInstance(){
        return grblController;
    }

    private void configureCrashReporting(){

    }

    public GoRestService getRetrofit(){
        if(goRestService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://gorest.co.in")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            goRestService = retrofit.create(GoRestService.class);
        }

        return goRestService;
    }

}
