package com.aacdemy.moonlight.controller;

import com.aacdemy.moonlight.config.security.JwtService;
import com.aacdemy.moonlight.dto.room.RoomFacilityRequestDto;
import com.aacdemy.moonlight.dto.room.RoomFacilityResponseDto;
import com.aacdemy.moonlight.dto.room.RoomFacilityUpdRequestDto;
import com.aacdemy.moonlight.entity.hotel.Room;
import com.aacdemy.moonlight.entity.hotel.RoomFacility;
import com.aacdemy.moonlight.entity.hotel.RoomType;
import com.aacdemy.moonlight.entity.hotel.RoomView;
import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.repository.hotel.RoomFacilityRepository;
import com.aacdemy.moonlight.repository.hotel.RoomRepository;
import com.aacdemy.moonlight.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomFacilityRepository facilityRepository;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Room roomStandard() {
        return Room.builder().id(1L)
                .roomNumber(100)
                .type(RoomType.STANDARD)
                .view(RoomView.SEA)
                .people(2)
                .price(BigDecimal.valueOf(220.0))
                .facilities(new ArrayList<>()).build();
    }

    private Room roomStudio() {
        return Room.builder().id(13L)
                .roomNumber(240)
                .type(RoomType.STUDIO)
                .view(RoomView.GARDEN)
                .people(3)
                .price(BigDecimal.valueOf(320.0))
                .facilities(new ArrayList<>()).build();
    }

    private Room roomApartment() {
        return Room.builder().id(17L)
                .roomNumber(350)
                .type(RoomType.APARTMENT)
                .view(RoomView.POOL)
                .people(4)
                .price(BigDecimal.valueOf(520.0))
                .facilities(new ArrayList<>()).build();
    }

    @Test
//    @WithMockUser(roles = "ADMIN")
    public void testFindByIdReturnOk() throws Exception {
        mockMvc.perform(get("/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(roomStandard())));
    }

    @Test
    public void testFindByIdReturnNotFound() throws Exception {
        mockMvc.perform(get("/rooms/255"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByRoomNumberReturnOk() throws Exception {
        mockMvc.perform(get("/rooms/room-number/100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(roomStandard())));
    }

    @Test
    public void testFindByRoomNumberReturnNotFound() throws Exception {
        mockMvc.perform((get("/rooms/room-number/255")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByRoomTypeReturnStandardOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rooms/room-type/STANDARD"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        Map<String, Object> objectMap = new LinkedHashMap<>();
        for (Field fieldName : roomStandard().getClass().getFields()) {
            objectMap.put(fieldName.getName(), fieldName.get(fieldName.getName()));
        }

        List<Map<String, Object>> result = List.of(objectMap);
        List<Map<String, Object>> responseList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        for (Map.Entry<String, Object> entry2 : result.listIterator().next().entrySet()) {
            for (Map<String, Object> entry : responseList) {
                String key = entry2.getKey();
                assertEquals(entry2.toString(), entry.get(key).toString());
            }
        }
    }

    @Test
    public void testFindByRoomTypeReturnStudioOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rooms/room-type/STUDIO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Map<String, Object> objectMap = new LinkedHashMap<>();
        for (Field fieldName : roomStandard().getClass().getFields()) {
            objectMap.put(fieldName.getName(), fieldName.get(fieldName.getName()));
        }

        List<Map<String, Object>> result = List.of(objectMap);
        List<Map<String, Object>> responseList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        for (Map.Entry<String, Object> entry2 : result.listIterator().next().entrySet()) {
            for (Map<String, Object> entry : responseList) {
                String key = entry2.getKey();
                assertEquals(entry2.toString(), entry.get(key).toString());
            }
        }
    }

    @Test
    public void testFindByRoomTypeReturnApartmentOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rooms/room-type/APARTMENT"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        Map<String, Object> objectMap = new LinkedHashMap<>();
        for (Field fieldName : roomStandard().getClass().getFields()) {
            objectMap.put(fieldName.getName(), fieldName.get(fieldName.getName()));
        }

        List<Map<String, Object>> result = List.of(objectMap);
        List<Map<String, Object>> responseList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        for (Map.Entry<String, Object> entry2 : result.listIterator().next().entrySet()) {
            for (Map<String, Object> entry : responseList) {
                String key = entry2.getKey();
                assertEquals(entry2.toString(), entry.get(key).toString());
            }
        }
    }

    @Test
    public void testFindByRoomTypeReturnNotFound() throws Exception {
        mockMvc.perform((get("/rooms/room-type/MESON")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByRoomViewReturnSeaOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rooms/room-view/SEA"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Map<String, Object> objectMap = new LinkedHashMap<>();
        for (Field fieldName : roomStandard().getClass().getFields()) {
            objectMap.put(fieldName.getName(), fieldName.get(fieldName.getName()));
        }

        List<Map<String, Object>> result = List.of(objectMap);
        List<Map<String, Object>> responseList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        for (Map.Entry<String, Object> entry2 : result.listIterator().next().entrySet()) {
            for (Map<String, Object> entry : responseList) {
                String key = entry2.getKey();
                assertEquals(entry2.toString(), entry.get(key).toString());
            }
        }
    }

    @Test
    public void testFindByRoomViewReturnPoolOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rooms/room-view/POOL"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testFindByRoomViewReturnGardenOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rooms/room-view/GARDEN"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        Map<String, Object> objectMap = new LinkedHashMap<>();
        for (Field fieldName : roomStandard().getClass().getFields()) {
            objectMap.put(fieldName.getName(), fieldName.get(fieldName.getName()));
        }

        List<Map<String, Object>> result = List.of(objectMap);
        List<Map<String, Object>> responseList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        for (Map.Entry<String, Object> entry2 : result.listIterator().next().entrySet()) {
            for (Map<String, Object> entry : responseList) {
                String key = entry2.getKey();
                assertEquals(entry2.toString(), entry.get(key).toString());
            }
        }
    }

    @Test
    public void testFindByRoomViewReturnNotFound() throws Exception {
        mockMvc.perform((get("/rooms/room-view/MOUNTAIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByPriceReturnOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rooms/room-price/220"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        Map<String, Object> objectMap = new LinkedHashMap<>();
        for (Field fieldName : roomStandard().getClass().getFields()) {
            objectMap.put(fieldName.getName(), fieldName.get(fieldName.getName()));
        }

        List<Map<String, Object>> result = List.of(objectMap);
        List<Map<String, Object>> responseList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        for (Map.Entry<String, Object> entry2 : result.listIterator().next().entrySet()) {
            for (Map<String, Object> entry : responseList) {
                String key = entry2.getKey();
                assertEquals(entry2.toString(), entry.get(key).toString());
            }
        }
    }

    @Test
    public void testFindByPriceReturnNotFound() throws Exception {
        mockMvc.perform((get("/rooms/room-price/102")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByPeopleReturnOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rooms/people/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Map<String, Object> objectMap = new LinkedHashMap<>();
        for (Field fieldName : roomStandard().getClass().getFields()) {
            objectMap.put(fieldName.getName(), fieldName.get(fieldName.getName()));
        }

        List<Map<String, Object>> result = List.of(objectMap);
        List<Map<String, Object>> responseList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        for (Map.Entry<String, Object> entry2 : result.listIterator().next().entrySet()) {
            for (Map<String, Object> entry : responseList) {
                String key = entry2.getKey();
                assertEquals(entry2.toString(), entry.get(key).toString());
            }
        }
    }

    @Test
    public void testFindByPeopleReturnNotFound() throws Exception {
        mockMvc.perform((get("/rooms/people/6")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSearchRoomsByFacilityReturnOk() throws Exception {
        RoomFacility facility = facilityRepository.save(RoomFacility.builder().facility("WiFi").build());
        List<RoomFacility> facilities = List.of(facility);
        Room room = roomRepository.findByRoomNumber(100).get();
        room.setFacilities(facilities);
        roomRepository.save(room);
        facility.setRoom(List.of(room));
        facilityRepository.save(facility);

        MvcResult mvcResult = mockMvc.perform(get("/rooms/search/WiFi"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        Map<String, Object> objectMap = new LinkedHashMap<>();
        for (Field fieldName : roomStandard().getClass().getFields()) {
            objectMap.put(fieldName.getName(), fieldName.get(fieldName.getName()));
        }

        List<Map<String, Object>> result = List.of(objectMap);
        List<Map<String, Object>> responseList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        for (Map.Entry<String, Object> entry2 : result.listIterator().next().entrySet()) {
            for (Map<String, Object> entry : responseList) {
                String key = entry2.getKey();
                assertEquals(entry2.toString(), entry.get(key).toString());
            }
        }

    }

    @Test
    public void testSearchRoomsByFacilityReturnNotFound() throws Exception {
        mockMvc.perform((get("/rooms/search/CableTV")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteFacilityReturnNoContent() throws Exception {
        RoomFacility facility = facilityRepository.save(RoomFacility.builder().facility("WiFi").build());
        List<RoomFacility> facilities = List.of(facility);
        Room room = roomRepository.findByRoomNumber(100).get();
        room.setFacilities(facilities);
        roomRepository.save(room);
        facility.setRoom(List.of(room));
        facilityRepository.save(facility);

        mockMvc.perform(request(HttpMethod.DELETE, "/rooms/1/facilities").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(RoomFacilityRequestDto.builder().facility("WiFi").build())).header("Authorization", "Bearer " + jwtService.generateJwt(adminUser())))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteFacilityReturnNotFound() throws Exception {
        mockMvc.perform(request(HttpMethod.DELETE, "/rooms/120/facilities").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(RoomFacilityRequestDto.builder().facility("WiFi").build())).header("Authorization", "Bearer " + jwtService.generateJwt(adminUser())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteFacilityReturnForbidden() throws Exception {
        mockMvc.perform(request(HttpMethod.DELETE, "/rooms/1/facilities").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(RoomFacilityRequestDto.builder().facility("WiFi").build())).header("Authorization", "Bearer " + jwtService.generateJwt(clientUser())))
                .andExpect(status().isForbidden());
    }

//    @Test
//    public void testAddFacilityReturnCreated() throws Exception {
//        RoomFacility facility = facilityRepository.save(RoomFacility.builder().facility("WiFi").build());
//
//        MvcResult mvcResult = mockMvc.perform(request(HttpMethod.PUT, "/rooms/2/facilities").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(RoomFacilityRequestDto.builder().facility("WiFi").build())).header("Authorization", "Bearer " + jwtService.generateJwt(adminUser())))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Map<String, Object> objectMap = new LinkedHashMap<>();
//        for (Field fieldName : roomStandard().getClass().getFields()) {
//            objectMap.put(fieldName.getName(), fieldName.get(fieldName.getName()));
//        }
//
//        Map<String, Object> responseList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class);
//
//        for (Map.Entry<String, Object> entry2 : objectMap.entrySet()) {
//            for (Map.Entry<String, Object> entry : responseList.entrySet()) {
//                assertEquals(entry2.toString(), entry.getValue().toString());
//            }
//        }
//    }

    @Test
    public void testAddFacilityReturnNotFound() throws Exception {
        mockMvc.perform(request(HttpMethod.PUT, "/rooms/650/facilities").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(RoomFacilityRequestDto.builder().facility("WiFi").build())).header("Authorization", "Bearer " + jwtService.generateJwt(adminUser())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddFacilityReturnForbidden() throws Exception {
        mockMvc.perform(request(HttpMethod.PUT, "/rooms/1/facilities").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(RoomFacilityRequestDto.builder().facility("WiFi").build())).header("Authorization", "Bearer " + jwtService.generateJwt(clientUser())))
                .andExpect(status().isForbidden());
    }

    private User adminUser() {
        return userRepository.findByEmail("admin@example.com").get();
    }

    private User clientUser() {
        return userRepository.findByEmail("user@example.com").get();
    }
}
