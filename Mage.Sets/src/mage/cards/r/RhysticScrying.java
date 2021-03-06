/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.r;

import java.util.UUID;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.costs.Cost;
import mage.abilities.costs.mana.GenericManaCost;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.DrawCardSourceControllerEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.game.Game;
import mage.players.Player;

/**
 *
 * @author L_J
 */
public class RhysticScrying extends CardImpl {

    public RhysticScrying(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.SORCERY}, "{2}{U}{U}");

        // Draw three cards. Then, if any player pays {2}, discard three cards.
        this.getSpellAbility().addEffect(new DrawCardSourceControllerEffect(3));
        this.getSpellAbility().addEffect(new RhysticScryingEffect());
    }

    public RhysticScrying(final RhysticScrying card) {
        super(card);
    }

    @Override
    public RhysticScrying copy() {
        return new RhysticScrying(this);
    }
}

class RhysticScryingEffect extends OneShotEffect {

    public RhysticScryingEffect() {
        super(Outcome.Benefit);
        this.staticText = "Then if any player pays {2}, discard three cards";
    }

    public RhysticScryingEffect(final RhysticScryingEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        MageObject sourceObject = game.getObject(source.getSourceId());
        if (controller != null && sourceObject != null) {
            boolean result = true;
            boolean doEffect = false;
            Cost cost = new GenericManaCost(2);
            // check if any player is willing to pay
            for (UUID playerId : game.getState().getPlayersInRange(controller.getId(), game)) {
                Player player = game.getPlayer(playerId);
                if (player != null && cost.canPay(source, source.getSourceId(), player.getId(), game) && player.chooseUse(Outcome.Detriment, "Pay " + cost.getText() + " for " + sourceObject.getLogName() + "?", source, game)) {
                    cost.clearPaid();
                    if (cost.pay(source, game, source.getSourceId(), player.getId(), false, null)) {
                        if (!game.isSimulation()) {
                            game.informPlayers(player.getLogName() + " pays the cost for " + sourceObject.getLogName());
                        }
                        doEffect = true;
                    }
                }
            }
            // do the effects if anybody paid
            if (doEffect) {
                controller.discard(3, false, source, game);
            }
            return result;
        }
        return false;
    }

    @Override
    public RhysticScryingEffect copy() {
        return new RhysticScryingEffect(this);
    }
}
