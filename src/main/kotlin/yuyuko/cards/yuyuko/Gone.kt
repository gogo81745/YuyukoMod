package yuyuko.cards.yuyuko

import basemod.abstracts.CustomCard
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import yuyuko.event.ApplyDiaphaneityPowerEvent
import yuyuko.event.ApplyDiaphaneityPowerEvent.ApplyDiaphaneityPower.CARD
import yuyuko.event.EventDispenser
import yuyuko.patches.CardColorEnum
import yuyuko.powers.DiaphaneityPower

class Gone : CustomCard(
        ID, NAME, IMAGE_PATH, COST, DESCRIPTION,
        CardType.SKILL, CardColorEnum.YUYUKO_COLOR,
        CardRarity.COMMON, CardTarget.ALL_ENEMY
) {
    companion object {
        @JvmStatic
        val ID = "Gone"
        val IMAGE_PATH = "images/yuyuko/cards/skill3.png"
        val COST = 1
        private val CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID)
        val NAME = CARD_STRINGS.NAME!!
        val DESCRIPTION = CARD_STRINGS.DESCRIPTION!!
    }


    override fun makeCopy(): AbstractCard = Gone()

    override fun use(self: AbstractPlayer?, target: AbstractMonster?) {
        val amount = self!!.getPower(DiaphaneityPower.POWER_ID)?.amount ?: return
        AbstractDungeon.getCurrRoom().monsters.monsters
                .filter { !it.isDeadOrEscaped }
                .forEach {
                    EventDispenser.emit(ApplyDiaphaneityPowerEvent(it, self, amount, CARD))

                }
    }

    override fun upgrade() {
        if (!this.upgraded) {
            this.upgradeName()
            this.upgradeBaseCost(0)
        }
    }


}