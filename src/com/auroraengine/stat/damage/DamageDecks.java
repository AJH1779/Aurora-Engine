/*
 * Copyright (C) 2017 LittleRover
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.auroraengine.stat.damage;

import com.auroraengine.debug.AuroraLogs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author LittleRover
 */
public class DamageDecks {
	private static final int CRIT_DECK_SIZE = 30;

	private static final Logger LOG = AuroraLogs.getLogger(DamageDecks.class
					.getName());
	private static final int NORM_DECK_SIZE = 80;

	public DamageDecks(
					BiFunction<ElementType, Integer, DamageCard> non_lethal_norm_gen,
					BiFunction<ElementType, Integer, DamageCard> non_lethal_crit_gen,
					BiFunction<ElementType, Integer, DamageCard> lethal_norm_gen,
					BiFunction<ElementType, Integer, DamageCard> lethal_crit_gen) {
		for (ElementType type : ElementType.values()) {
			ArrayList<DamageCard> non_lethal_norm_list = new ArrayList<>(
							NORM_DECK_SIZE);
			ArrayList<DamageCard> non_lethal_crit_list = new ArrayList<>(
							CRIT_DECK_SIZE);
			ArrayList<DamageCard> lethal_norm_list = new ArrayList<>(NORM_DECK_SIZE);
			ArrayList<DamageCard> lethal_crit_list = new ArrayList<>(CRIT_DECK_SIZE);
			non_lethal_norm.put(type, non_lethal_norm_list);
			non_lethal_crit.put(type, non_lethal_crit_list);
			lethal_norm.put(type, lethal_norm_list);
			lethal_crit.put(type, lethal_crit_list);
			for (int i = 0; i < NORM_DECK_SIZE; i++) {
				non_lethal_norm_list.add(non_lethal_norm_gen.apply(type, i));
				lethal_norm_list.add(lethal_norm_gen.apply(type, i));
			}
			for (int i = 0; i < CRIT_DECK_SIZE; i++) {
				non_lethal_crit_list.add(non_lethal_crit_gen.apply(type, i));
				lethal_crit_list.add(lethal_crit_gen.apply(type, i));
			}
			non_lethal_norm_list.trimToSize();
			non_lethal_crit_list.trimToSize();
			lethal_norm_list.trimToSize();
			lethal_crit_list.trimToSize();
		}
	}
	private final HashMap<ElementType, ArrayList<DamageCard>> lethal_crit = new HashMap<>(
					ElementType.values().length);
	private final HashMap<ElementType, ArrayList<DamageCard>> lethal_norm = new HashMap<>(
					ElementType.values().length);
	private final HashMap<ElementType, ArrayList<DamageCard>> non_lethal_crit = new HashMap<>(
					ElementType.values().length);
	private final HashMap<ElementType, ArrayList<DamageCard>> non_lethal_norm = new HashMap<>(
					ElementType.values().length);

	public Stream<DamageCard> deal(ElementType type, int i) {
		return deal(type, i, false, true);
	}

	public Stream<DamageCard> deal(ElementType type, int i, boolean crit) {
		return deal(type, i, crit, true);
	}

	public Stream<DamageCard> deal(ElementType type, int i, boolean crit,
																 boolean lethal) {
		if (i < 0) {
			throw new IllegalArgumentException("Cannot Deal Negative Cards");
		} else if (i == 0) {
			return Stream.empty();
		}
		ArrayList<DamageCard> list
						= (crit ? (lethal ? lethal_crit : non_lethal_crit) :
							 (lethal ? lethal_norm : non_lethal_crit)).get(type);
		Collections.shuffle(list);
		if (i >= list.size()) {
			return list.stream();
		} else {
			return list.stream().limit(i);
		}
	}

	/*public static final DamageDecks DEFAULT = new DamageDecks(
			new BiFunction<ElementType, Integer, DamageCard>() {
		@Override
		public DamageCard apply(ElementType type, Integer i) {
			// Non-Lethal-Norm
			DamageCard card = new DamageCard(type);

			//<editor-fold defaultstate="collapsed" desc="HP, SP, CHP Damage">
			int hp_dmg = 1 << (i / 8);
			int sp_dmg = 1 << ((NORM_DECK_SIZE - i - 1) / 8);
			int nl_chp_dmg = i == NORM_DECK_SIZE ? 1 : 0; // Non-Lethal
			int chp_dmg = ((i % 8) == 0) ? (i == NORM_DECK_SIZE ? 2 : 1) : 0; // Lethal

			switch (type) {
				case SLASH:
				case BLUNT:
				case FIRE:
				case ICE:
					card.setHPDamage(2 * hp_dmg);
					break;
				case PIERCE:
				case EARTH:
				case ACID:
				case ABSORPTION:
				case TRANSMUTE:
				case ELECTRIC:
					card.setHPDamage(hp_dmg);
					break;
				case WATER:
					card.setHPDamage(- 2 * hp_dmg);
					break;
				case GENETIC:
					card.setHPDamage(-hp_dmg);
					break;
			}
			switch (type) {
				case BLUNT:
				case PIERCE:
				case ICE:
				case EARTH:
					card.setSPDamage(2 * sp_dmg);
					break;
				case SLASH:
				case FIRE:
				case ACID:
				case ABSORPTION:
				case TRANSMUTE:
					card.setSPDamage(sp_dmg);
					break;
				case WIND:
					card.setSPDamage(- 2 * sp_dmg);
					break;
				case GENETIC:
					card.setSPDamage(- sp_dmg);
					break;
			}
			switch (type) {
				case SLASH:
				case PIERCE:
				case FIRE:
				case EARTH:
					card.setCHPDamage(nl_chp_dmg);
					break;
				case ACID:
				case ABSORPTION:
				case TRANSMUTE:
					card.setCHPDamage(chp_dmg);
					break;
				case LIFE:
					card.setCHPDamage(- chp_dmg);
					break;
				case GENETIC:
					card.setCHPDamage(- nl_chp_dmg);
					break;
			}
			//</editor-fold>

			if(type == ElementType.PSYCHIC) {
				card.addStatusEffect(ElementType.PSYCHIC.getStatusEffects().get(i % ElementType.PSYCHIC.getStatusEffects().size()));
			}

			return card;
		}
	},
			(type, i) -> { // Non-Lethal-Crit

			},
			(type, i) -> { // Lethal-Norm

			},
			(type, i) -> { // Lethal-Crit

			}
	);*/
}
