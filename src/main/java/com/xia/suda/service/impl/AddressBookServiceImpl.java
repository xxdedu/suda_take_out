package com.xia.suda.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xia.suda.entity.AddressBook;
import com.xia.suda.mapper.AddressBookMapper;
import com.xia.suda.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: codedong
 * @Date:2022年07月25日22:30
 */

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
