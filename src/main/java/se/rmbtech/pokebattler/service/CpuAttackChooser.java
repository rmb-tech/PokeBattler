package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.exception.InvalidPokemonException;
import se.rmbtech.pokebattler.model.Attack;
import se.rmbtech.pokebattler.model.Pokemon;

import java.util.List;
import java.util.Random;

public class CpuAttackChooser implements AttackChooser {
    private final Random random;

    public CpuAttackChooser() {
        this(new Random());
    }

    public CpuAttackChooser(Random random) {
        this.random = random;
    }

    @Override
    public Attack chooseAttack(Pokemon attacker, Pokemon defender) {
        List<Attack> attacks = attacker.getAttacks();
        if (attacks.isEmpty()) {
            throw new InvalidPokemonException(attacker.getName() + " har inga attacker.");
        }

        List<Attack> useful = attacks.stream()
                .filter(a -> a.isUsefulAgainst(defender))
                .toList();
        List<Attack> pool = useful.isEmpty() ? attacks : useful;

        System.out.println("\n--- MOTSTÅNDARENS TUR ---");
        return pool.get(random.nextInt(pool.size()));
    }
}