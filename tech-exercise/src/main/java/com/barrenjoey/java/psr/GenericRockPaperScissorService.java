package com.barrenjoey.java.psr;

import com.barrenjoey.java.psr.model.Move;
import com.barrenjoey.java.psr.model.Result;

import java.util.List;

public interface GenericRockPaperScissorService {
    Result playGame(List<Move> moves);
}
