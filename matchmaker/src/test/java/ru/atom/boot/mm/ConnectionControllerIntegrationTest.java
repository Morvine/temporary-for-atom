package ru.atom.boot.mm;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ConnectionControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void join() throws Exception {
        mockMvc.perform(post("/matchmaker/join")
                .content("name=a")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
    }

    @Test
    public void connect() throws Exception {
        mockMvc.perform(post("/matchmaker/gameId")
                .content("gameId=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
    }

    @Test
    public void getId() throws Exception {
        mockMvc.perform(get("/matchmaker/gameId"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("gameId=1"));
    }

    @Test
    public void mockTest() throws Exception {
        mockMvc.perform(post("/matchmaker/join")
                .content("name=a")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        //Thread.sleep(2000);

        mockMvc.perform(post("/matchmaker/gameId")
                .content("gameId=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        mockMvc.perform(post("/matchmaker/join")
                .content("name=b")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        mockMvc.perform(get("/matchmaker/gameId"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("gameId=1"));




        mockMvc.perform(post("/matchmaker/join")
                .content("name=d")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());


        mockMvc.perform(post("/matchmaker/gameId")
                .content("gameId=2")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());



        mockMvc.perform(post("/matchmaker/join")
                .content("name=c")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        mockMvc.perform(post("/matchmaker/gameId")
                .content("gameId=2")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
    }
}