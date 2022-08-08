package com.tdd.productsupport.feedback.service;

import com.tdd.productsupport.feedback.model.Feedback;
import com.tdd.productsupport.feedback.repository.FeedbackRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FeedbackServiceTests {

    @Autowired
    private FeedbackService feedbackService;

    @MockBean
    private FeedbackRepository feedbackRepository;

    @Test
    @DisplayName("Find feedback by id successfully")
    void testFindFeedbackById() {
        // Prepare mock feedback
        Feedback mockFeedback = new Feedback("1", 1, 1, "POSTED", "This product is great!");

        // Prepare the mock repository call
        doReturn(Optional.of(mockFeedback)).when(feedbackRepository).findById("1");

        // When
        Optional<Feedback> foundFeedback = feedbackService.findById("1");

        // Then
        Assertions.assertTrue(foundFeedback.isPresent());
        Assertions.assertNotEquals(Optional.empty(), foundFeedback);
        foundFeedback.ifPresent(f -> {
            Assertions.assertEquals(1, f.getProductId().intValue());
            Assertions.assertEquals("POSTED", f.getStatus());
        });
    }

    @Test
    @DisplayName("Find feedback by product id successfully")
    void testFindFeedbackByProductId() {
        // Prepare mock feedback
        Feedback mockFeedback = new Feedback("1", 1, 1, "POSTED", "This product is great!");

        // Prepare the mock repository call
        doReturn(Optional.of(mockFeedback)).when(feedbackRepository).findByProductId(1);

        // When
        Optional<Feedback> foundFeedback = feedbackService.findByProductId(1);

        // Then
        Assertions.assertTrue(foundFeedback.isPresent());
        Assertions.assertNotEquals(Optional.empty(), foundFeedback);
        foundFeedback.ifPresent(f -> {
            Assertions.assertEquals("1", f.getId());
            Assertions.assertEquals("POSTED", f.getStatus());
            Assertions.assertEquals(1, f.getUserId().intValue());
        });
    }

    @Test
    @DisplayName("Find feedback by id failure")
    void testFindFeedbackByIdFailure() {
        // Prepare the mock repository call
        doReturn(Optional.empty()).when(feedbackRepository).findByProductId(1);

        // When
        Optional<Feedback> foundFeedback = feedbackService.findByProductId(1);

        // Then
        Assertions.assertFalse(foundFeedback.isPresent());
        Assertions.assertEquals(Optional.empty(), foundFeedback);
    }

    @Test
    @DisplayName("Find all feedback successfully")
    void testFindAllFeedback() {
        // Prepare mock feedback
        Feedback mockFeedback1 = new Feedback("1", 1, 1, "POSTED", "This product is great!");
        Feedback mockFeedback2 = new Feedback("2", 1, 2, "POSTED", "This product is awesome!");

        // Prepare the mock repository call
        doReturn(Arrays.asList(mockFeedback1, mockFeedback2)).when(feedbackRepository).findAll();

        // When
        List<Feedback> feedbackList = feedbackService.findAll();

        // Then
        Assertions.assertNotNull(feedbackList);
        Assertions.assertEquals(2, feedbackList.size());
    }

    @Test
    @DisplayName("Save new feedback successfully")
    void testSaveNewFeedback() {
        // Prepare mock feedback
        Feedback feedbackToSave = new Feedback("1", 1, 1, "POSTED", "This product is great!");
        Feedback feedbackToReturn = new Feedback("1", 1, 1, "POSTED", "This product is great!");

        // Prepare the mock repository call
        doReturn(feedbackToReturn).when(feedbackRepository).save(feedbackToSave);

        // When
        Feedback savedFeedback = feedbackService.save(feedbackToSave);

        // Then
        Assertions.assertNotNull(savedFeedback);
        Assertions.assertEquals(1, savedFeedback.getVersion().intValue());
    }

    @Test
    @DisplayName("Delete feedback successfully")
    void testDeleteFeedbackSuccessfully() {
        // Prepare mock feedback

        // Prepare mock repository call
        doReturn(Optional.empty()).when(feedbackRepository).findById("1");

        // When
        feedbackService.delete("1");
        Optional<Feedback> feedback = feedbackService.findById("1");
        assertThat(feedback).isNotPresent();
    }
}
