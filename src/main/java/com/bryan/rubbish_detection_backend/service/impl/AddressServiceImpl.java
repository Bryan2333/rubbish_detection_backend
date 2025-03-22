package com.bryan.rubbish_detection_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bryan.rubbish_detection_backend.entity.Address;
import com.bryan.rubbish_detection_backend.mapper.AddressMapper;
import com.bryan.rubbish_detection_backend.service.AddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {
}
