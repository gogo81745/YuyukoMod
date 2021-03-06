package yuyuko.cards.yuyuko

import basemod.abstracts.CustomCard
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.SLASH_HEAVY
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType.NORMAL
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import yuyuko.cards.isButterfly
import yuyuko.patches.CardColorEnum

class InfiniteSin : CustomCard(
        ID, NAME, IMAGE_PATH, COST, DESCRIPTION,
        CardType.ATTACK, CardColorEnum.YUYUKO_COLOR,
        CardRarity.UNCOMMON, CardTarget.ENEMY
) {
    companion object {
        @JvmStatic
        val ID = "Infinite Sin"
        val IMAGE_PATH = "images/yuyuko/cards/attack4.png"
        val COST = 1
        private val CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID)
        val NAME = CARD_STRINGS.NAME!!
        val DESCRIPTION = CARD_STRINGS.DESCRIPTION!!
        val UPGRADE_DESCRIPTION = CARD_STRINGS.UPGRADE_DESCRIPTION!!
    }

    override fun makeCopy(): AbstractCard = InfiniteSin()

    override fun calculateCardDamage(mo: AbstractMonster?) {
        val player = AbstractDungeon.player
        val groups = listOf(player.hand, player.drawPile, player.discardPile)
        val count = groups
                .map {
                    it.group.count { it.isButterfly() }
                }
                .reduce { acc, i -> acc + i }

        this.baseDamage = count * 10
        super.calculateCardDamage(mo)
    }

    override fun use(self: AbstractPlayer?, target: AbstractMonster?) {
        listOf(self!!.hand, self.drawPile, self.discardPile)
                .map {
                    it to it.group.filter { it.isButterfly() }
                }
                .forEach { (group, cards) ->
                    cards.forEach {
                        AbstractDungeon.actionManager.addToBottom(
                                ExhaustSpecificCardAction(it, group)
                        )
                    }
                }

        AbstractDungeon.actionManager.addToBottom(
                DamageAction(
                        target,
                        DamageInfo(self, this.damage, NORMAL),
                        SLASH_HEAVY
                )
        )
    }

    override fun triggerWhenDrawn() {
        calculateCardDamage(null)
    }

    override fun upgrade() {
        if (!this.upgraded) {
            this.upgradeName()
            this.rawDescription = UPGRADE_DESCRIPTION
            this.initializeDescription()
        }
    }

}