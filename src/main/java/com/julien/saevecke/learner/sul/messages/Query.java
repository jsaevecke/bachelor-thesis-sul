package com.julien.saevecke.learner.sul.messages;

import com.julien.saevecke.learner.sul.config.mealymachines.coffee.Input;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Query {
    private Input input;
}
