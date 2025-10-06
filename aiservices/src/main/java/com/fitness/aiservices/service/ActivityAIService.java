package com.fitness.aiservices.service;

import com.fitness.aiservices.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public String generateRecommendation(Activity activity){

        String prompt=createPromptForActivity(activity);
        String aiResposne=geminiService.getAnswer(prompt);
        log.info("AI Response: {}", aiResposne);
        return aiResposne;
    }

    private  String createPromptForActivity(Activity activity){

    return  String.format("""
            Analyze this fitness activty and provide detailed recommendations in this format
            {
            "analysis":{
                        "overall": "Overall analysis here"
                        "pace": "Pace analysis here",
                        "heartRate": "Heart rate analysis here",
                        "caloriesBurned": "Calories analysis here",
                        },
            "improvements":[{
                        "area": "Area name",
                        "recommendation":"Detailed recommendation"
                        }
                        ],
            "suggestions":[{
                "workout": "Workout name",
                "description": "Detail workout description"
            }
            ],
            "safety": [
           "Safety point 1",
           "Safety point 2"
            ]
            }
           Analyze this activity:
           Activity Type: %s
           Duration: %d minutes
           Calories Burned: %d
           Additional Metrics: %s
           Provide detailed analysis focusing on performance, improvements, next workout recommendation in short lines
           Ensure the resposne follow the EXACT JSON format shown above
           """,activity.getType(),
            activity.getDuration(),
            activity.getCaloriesBurned(),
            activity.getAddiontalMetries());

    }

}
