package com.tencent.wink.storage.winkdb.utils;

import com.tencent.wink.storage.winkdb.WinkRoomDatabase;
import com.tencent.wink.storage.winkdb.dao.LibraryDao;
import com.tencent.wink.storage.winkdb.dao.MusicListDao;
import com.tencent.wink.storage.winkdb.dao.UserDao;
import com.tencent.wink.storage.winkdb.dao.WordDao;
import com.tencent.wink.storage.winkdb.model.Address;
import com.tencent.wink.storage.winkdb.model.Department;
import com.tencent.wink.storage.winkdb.model.Job;
import com.tencent.wink.storage.winkdb.model.Library;
import com.tencent.wink.storage.winkdb.model.MusicList;
import com.tencent.wink.storage.winkdb.model.User;
import com.tencent.wink.storage.winkdb.model.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据生成类
 * Created by walkerzpli on 2022/1/23.
 */
public class DataFactoryUtil {


    public static void initWords(WinkRoomDatabase database) {

        WordDao dao = database.wordDao();
        dao.deleteAll();

        Word word = new Word("Hello");
        Address address = new Address("10086");
        address.street = "兴华路";
        address.state = "广东省";
        address.city = "深圳";

        dao.insert(word);

        word = new Word("World");
        dao.insert(word);
    }

    public static void initUsers(WinkRoomDatabase database) {

        UserDao userDao = database.userDao();
        LibraryDao libraryDao = database.libraryDao();
        MusicListDao musicListDao = database.musicListDao();

        // init users
        User user1 = new User();
        user1.firstName = "walker";
        user1.lastName = "lee";
        Address address1 = new Address("424500");
        address1.street = "粤海街道";
        address1.state = "广东省";
        address1.city = "深圳市";
        user1.address = address1;
        Department department1 = new Department();
        department1.id = 20001;
        department1.name = "pony";
        user1.department = department1;
        user1.company = Arrays.asList("baidu", "tencent");
        Map<Integer, Job> map1 = new HashMap<>();
        Job job1 = new Job("百度", 1.5);
        job1.address = address1;
        map1.put(1, job1);
        Job job2= new Job("腾讯", 2);
        job1.address = address1;
        map1.put(2, job2);
        user1.jobs = map1;

        User user2 = new User();
        user2.firstName = "cherry";
        user2.lastName = "yan";
        Address address2 = new Address("424501");
        address2.street = "岳麓街道";
        address2.state = "湖南省";
        address2.city = "长沙市";
        user2.address = address2;

        userDao.insertAll(user1, user2);

        // init users
        User user3 = new User();
        user3.firstName = "jack";
        user3.lastName = "lee";
        Address address3 = new Address("424500");
        address3.street = "粤海街道";
        address3.state = "广东省";
        address3.city = "深圳市";
        user3.address = address3;
        Department department3 = new Department();
        department3.id = 20001;
        department3.name = "pony";
        user3.department = department3;
        user3.company = Arrays.asList("baidu", "tencent");
        Map<Integer, Job> map3 = new HashMap<>();
        Job job31 = new Job("百度", 1.5);
        job31.address = address3;
        map3.put(1, job31);
        Job job32= new Job("腾讯", 2);
        job32.address = address3;
        map3.put(2, job32);
        user3.jobs = map3;

        User user4 = new User();
        user4.firstName = "jack";
        user4.lastName = "wan";
        Address address4 = new Address("424501");
        address4.street = "岳麓街道";
        address4.state = "湖南省";
        address4.city = "长沙市";
        user4.address = address4;


        List<User> users = new ArrayList<>();
        users.add(user3);
        users.add(user4);
        userDao.insertAll(users);

        // init library
        Library library1 = new Library();
        library1.libraryID = 10086L;
        library1.userID = 1;
        libraryDao.insert(library1);

        Library library2 = new Library();
        library2.libraryID = 10087L;
        library2.userID = 1;
        libraryDao.insert(library2);

        // init music list
        MusicList musicList1 = new MusicList();
        musicList1.id = 100001;
        musicList1.listName = "jack zhou";
        musicList1.userID = 2;

        MusicList musicList2 = new MusicList();
        musicList2.id = 100002;
        musicList2.listName = " zhou";
        musicList2.userID = 2;

        musicListDao.insert(musicList1, musicList2);

    }
}
