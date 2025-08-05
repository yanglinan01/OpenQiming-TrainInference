<template>
  <div>
    <MenuItem :to="menu.path" :replace="menu.replace" :target="menu.target"
              :name="menu.path" @click.native="handleClick(menu.path)">
      <i-menu-side-title :menu="menu" :hide-title="hideTitle"/>
    </MenuItem>
  </div>
</template>
<script>
import iMenuSideTitle from './menu-title';
import clickItem from '../mixins/click-item';
import tTitle from '../mixins/translate-title';

export default {
  name: 'iMenuSideItem',
  components: {iMenuSideTitle},
  mixins: [clickItem, tTitle],

  props: {
    menu: {
      type: Object,
      default() {
        return {}
      }
    },
    hideTitle: {
      type: Boolean,
      default: false
    }
  },
  data (){
    return {
      hideUserTitle:localStorage.getItem("systemAuth")||'1'
    }
  }
}
</script>
