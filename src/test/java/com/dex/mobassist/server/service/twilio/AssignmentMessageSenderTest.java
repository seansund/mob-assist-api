package com.dex.mobassist.server.service.twilio;


import com.dex.mobassist.server.cargo.AssignmentCargo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssignmentMessageSenderTest {
    @Nested
    @DisplayName("Given AssignmentMessageSender.buildAssignmentDiagram()")
    class GivenBuildAssignmentDiagram {
        @Nested
        @DisplayName("when called with 'Table 5 - J31, J19")
        class WhenCalledWithTable5J31J19 {
            @Test
            @DisplayName("then return 'table5,j31,j19'")
            void thenReturntable5j31j19() {
                final String actualValue = AssignmentMessageSender.buildAssignmentDiagramUrl("Table 5 - J31, J19");

                assertEquals("https://bit.ly/deacon-assn#table5,j31,j19", actualValue);
            }
        }
    }

    @Nested
    @DisplayName("Given AssignmentMessageSender.buildAssignmentDisplay()")
    class GivenBuildAssignmentDisplay {
        @Nested
        @DisplayName("when called with {group: 'Table 2', name: 'A1'}")
        class WhenCalledWithOneAssignment {
            @Test
            @DisplayName("then return 'Table 2 - A1'")
            void thenReturnFormattedValue() {
                Assertions.assertEquals(
                        "Table 2 - A1",
                        AssignmentMessageSender.buildAssignmentDisplay(
                                List.of(new AssignmentCargo().withGroup("Table 2").withName("A1").withRow(1))
                        )
                );
            }
        }

        @Nested
        @DisplayName("when called with [{g:'Table 2',n:'A1'},{g:'Table 2',n:'J11'}]")
        class WhenCalledWithTwoAssignmentsFromSameTable {
            @Test
            @DisplayName("then return 'Table 2 - A1, J11'")
            void thenReturnFormattedValue() {
                Assertions.assertEquals(
                        "Table 2 - A1, J11",
                        AssignmentMessageSender.buildAssignmentDisplay(
                                List.of(
                                        new AssignmentCargo().withGroup("Table 2").withName("A1").withRow(1),
                                        new AssignmentCargo().withGroup("Table 2").withName("J11").withRow(2)
                                )
                        )
                );

            }
        }

        @Nested
        @DisplayName("when called with [{g:'Table 2',n:'J11'},{g:'Table 2',n:'A1'}]")
        class WhenCalledWithTwoUnsortedAssignmentsFromSameGroup {
            @Test
            @DisplayName("then return 'Table 2 - A1, J11'")
            void thenReturnFormattedValue() {
                Assertions.assertEquals(
                        "Table 2 - A1, J11",
                        AssignmentMessageSender.buildAssignmentDisplay(
                                List.of(
                                        new AssignmentCargo().withGroup("Table 2").withName("J11").withRow(2),
                                        new AssignmentCargo().withGroup("Table 2").withName("A1").withRow(1)
                                )
                        )
                );

            }
        }

        @Nested
        @DisplayName("when called with [{g:'Table 4',n:'F23'},{g:'Table 2',n:'F11'}]")
        class WhenCalledWithTwoUnsortedAssignmentsFromDifferentGroups {
            @Test
            @DisplayName("then return 'Table 2 - A1, J11'")
            void thenReturnFormattedValue() {
                Assertions.assertEquals(
                        "Table 2 - F11, F23",
                        AssignmentMessageSender.buildAssignmentDisplay(
                                List.of(
                                        new AssignmentCargo().withGroup("Table 4").withName("F23").withRow(3),
                                        new AssignmentCargo().withGroup("Table 2").withName("F11").withRow(2)
                                )
                        )
                );

            }
        }
    }
}
